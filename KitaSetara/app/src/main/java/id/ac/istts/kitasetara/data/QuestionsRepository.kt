package id.ac.istts.kitasetara.data

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.model.quiz.Question
import id.ac.istts.kitasetara.model.term.Term
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

    // Get all questions from room/local database
    fun getAllQuestions(): List<Question> {
        return localDatasource.questionDao().getAll()
    }
}