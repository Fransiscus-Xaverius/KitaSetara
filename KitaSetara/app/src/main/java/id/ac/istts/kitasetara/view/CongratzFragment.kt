package id.ac.istts.kitasetara.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentCongratzBinding
import id.ac.istts.kitasetara.model.highscore.Highscore


class CongratzFragment : Fragment() {

    private var _binding: FragmentCongratzBinding? = null
    private val binding get() = _binding!!
    private val args: CongratzFragmentArgs by navArgs<CongratzFragmentArgs>()
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCongratzBinding.inflate(inflater, container, false)
        db = FirebaseDatabase.getInstance()
        auth = Firebase.auth
        playAnimation()
        return binding.root
    }

    private fun playAnimation(){
        val congratzTitle = ObjectAnimator.ofFloat(binding.tvCongrats, View.ALPHA,1f).setDuration(250)
        val ivTrophy = ObjectAnimator.ofFloat(binding.ivTrophy,View.ALPHA,1f).setDuration(250)
        val tvYouscore = ObjectAnimator.ofFloat(binding.tvYouscore,View.ALPHA,1f).setDuration(250)
        val tvScore = ObjectAnimator.ofFloat(binding.tvScore,View.ALPHA,1f).setDuration(250)
        val btnSave = ObjectAnimator.ofFloat(binding.btnSaveScore,View.ALPHA,1f).setDuration(250)
        val btnBack = ObjectAnimator.ofFloat(binding.btnCongratzBack,View.ALPHA,1f).setDuration(250)
        val together = AnimatorSet().apply {
            playTogether(tvYouscore, tvScore)
        }

        val together2 = AnimatorSet().apply {
            playTogether(btnSave, btnBack)
        }
        //start animation
        AnimatorSet().apply {
            playSequentially(congratzTitle,ivTrophy,together,together2)
            start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scoresRef = db.getReference("scores")
        binding.apply {
            tvScore.text = "${args.score}/100"
            btnCongratzBack.setOnClickListener {
                findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
            }
            btnSaveScore.setOnClickListener {
                //save score to firebase
                //check whether user has internet connection
                if (Helper.isInternetAvailable(requireActivity())) {
                    //check user id
                    var userId = ""
                    var name = ""
                    var photoUrl = ""
                    if (Helper.currentUser != null) {
                        //user logged in without google
                        userId = Helper.currentUser!!.id!!
                        name = Helper.currentUser!!.name
                        photoUrl = Helper.currentUser!!.imageUrl!!
                    } else {
                        //user logged in with google
                        val currentUser = auth.currentUser
                        userId = currentUser!!.uid
                        name = currentUser.displayName!!
                        photoUrl = currentUser.photoUrl.toString()
                    }
                    val newScore = args.score
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
                                    scoreRef.child("score").setValue(totalScore)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Helper.showSnackbar(
                                                    requireActivity().findViewById(android.R.id.content),
                                                    "Score successfully saved!"
                                                )
                                                findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
                                            } else {
                                                task.exception?.let {
                                                    Helper.showSnackbar(
                                                        requireActivity().findViewById(android.R.id.content),
                                                        "Score could not be saved: ${it.message}"
                                                    )
                                                }
                                            }
                                        }
                                }
                            } else {
                                //insert a brand-new score
                                val highscore = Highscore(newScore, userId, name,photoUrl)
                                scoresRef.push().setValue(highscore) { error, _ ->
                                    if (error != null) {
                                        Helper.showSnackbar(
                                            requireActivity().findViewById(android.R.id.content),
                                            "Something wrong happened!"
                                        )
                                    } else {
                                        Helper.showSnackbar(
                                            requireActivity().findViewById(android.R.id.content),
                                            "Score successfully saved!"
                                        )
                                        findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Helper.showSnackbar(
                                requireActivity().findViewById(android.R.id.content),
                                "Database Error : ${error.message}"
                            )
                        }
                    })

                } else {
                    //call showsnackbar function which resides in Helper.kt
                    Helper.showSnackbar(
                        requireActivity().findViewById(android.R.id.content),
                        "No internet connection!"
                    )
                }

            }

        }
    }


}