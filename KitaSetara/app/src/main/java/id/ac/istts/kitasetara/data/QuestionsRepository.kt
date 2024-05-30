package id.ac.istts.kitasetara.data

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.model.forum.User
import id.ac.istts.kitasetara.model.highscore.Highscore
import id.ac.istts.kitasetara.model.quiz.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuestionsRepository(private val firebaseDatabase: FirebaseDatabase, private val localDatasource : AppDatabase) {
    // Fetch data from Firebase and save it to Room Database
    suspend fun fetchQuestionsAndSaveToLocalDatabase() {
        firebaseDatabase.getReference("questions").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                for (snapshot in dataSnapshot.children) {
                    //get question
                    val question = snapshot.child("question").getValue(String::class.java) ?: ""
                    //get answer
                    val answer = snapshot.child("answer").getValue(String::class.java) ?: ""
                    val opt1 = snapshot.child("ans1").getValue(String::class.java)
                    val opt2 = snapshot.child("ans2").getValue(String::class.java)
                    val opt3 = snapshot.child("ans3").getValue(String::class.java)
                    val opt4 = snapshot.child("ans4").getValue(String::class.java)
                    questions.add(Question(id=snapshot.key!!,question=question, answer = answer, option1 =opt1, option2 = opt2, option3 = opt3, option4 = opt4))
                }
                // Insert questions into Room Database
                CoroutineScope(Dispatchers.IO).launch {
                    localDatasource.questionDao().insertAll(questions.shuffled().take(10))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun saveScoreToFirebase(context: Context, auth: FirebaseAuth, newScore : Int,db:FirebaseDatabase,fragment: Fragment) {
        val view = context as Activity
        //check whether user has internet connection
        if (Helper.isInternetAvailable(context)) {
            //check user id
            var userId = ""
            var name = ""
            var photoUrl = ""
            if (Helper.currentUser != null) {
                //user logged in without google
                userId = Helper.currentUser!!.id!!
                name = Helper.currentUser!!.name
//                photoUrl = Helper.currentUser!!.imageUrl!!
            } else {
                //user logged in with google
                val currentUser = auth.currentUser
                userId = currentUser!!.uid
                name = currentUser.displayName!!
//                photoUrl = currentUser.photoUrl.toString()
            }

            val auth = Firebase.auth
            val user = auth.currentUser //get current user that has logged in with google
            var uid = ""
            uid = if (Helper.currentUser != null){
                Helper.currentUser!!.id!!
            }else{
                //login with google
                user!!.uid
            }
            // Reference to the "users" node in the Realtime Database
            val usersRef = firebaseDatabase.reference.child("users")
            // Reference to the specific user's node using their user ID
            val userRef = usersRef.child(uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.getValue(User::class.java)
                        photoUrl = userData!!.imageUrl!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                    Helper.showSnackbar(
                        view.findViewById(android.R.id.content),
                        "Database Error : ${error.message}"
                    )
                }
            })

            val scoresRef = db.getReference("scores")
            val query = scoresRef.orderByChild("userid").equalTo(userId)

            query.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (scoreSnapshot in snapshot.children) {
                            val scoreId = scoreSnapshot.key!!
                            val existingScore =
                                scoreSnapshot.child("score").getValue(Int::class.java)
                            val totalScore = newScore + existingScore!!
                            val scoreRef = scoresRef.child(scoreId)

                            val updates: MutableMap<String, Any> = HashMap()
                            updates["photoUrl"] = photoUrl
                            updates["score"] = totalScore

                            scoreRef.updateChildren(updates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Helper.showSnackbar(
                                            view.findViewById(android.R.id.content),
                                            "Score successfully saved!"
                                        )

                                        fragment.findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
                                    } else {
                                        task.exception?.let {
                                            Helper.showSnackbar(
                                                view.findViewById(android.R.id.content),
                                                "Score could not be saved: ${it.message}"
                                            )
                                        }
                                    }
                                }

//                            scoreRef.child("score").setValue(totalScore)
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        Helper.showSnackbar(
//                                            view.findViewById(android.R.id.content),
//                                            "Score successfully saved!"
//                                        )
//
//                                        fragment.findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
//                                    } else {
//                                        task.exception?.let {
//                                            Helper.showSnackbar(
//                                                view.findViewById(android.R.id.content),
//                                                "Score could not be saved: ${it.message}"
//                                            )
//                                        }
//                                    }
//                                }
                        }
                    } else {
                        //insert a brand-new score
                        val highscore = Highscore(newScore, userId, name,photoUrl)
                        scoresRef.push().setValue(highscore) { error, _ ->
                            if (error != null) {
                                Helper.showSnackbar(
                                    view.findViewById(android.R.id.content),
                                    "Something wrong happened!"
                                )
                            } else {
                                Helper.showSnackbar(
                                    view.findViewById(android.R.id.content),
                                    "Score successfully saved!"
                                )

                                fragment.findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Helper.showSnackbar(
                        view.findViewById(android.R.id.content),
                        "Database Error : ${error.message}"
                    )
                }
            })

        } else {
            //call showsnackbar function which resides in Helper.kt
            Helper.showSnackbar(
                view.findViewById(android.R.id.content),
                "No internet connection!"
            )
        }
    }


    // Get all questions from room/local database
    fun getAllQuestions(): List<Question> {
        return localDatasource.questionDao().getAll()
    }

}