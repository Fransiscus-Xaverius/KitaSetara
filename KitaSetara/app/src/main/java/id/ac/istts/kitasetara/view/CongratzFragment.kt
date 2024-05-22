package id.ac.istts.kitasetara.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentCongratzBinding
import id.ac.istts.kitasetara.viewmodel.QuizViewModel


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

        val viewModel : QuizViewModel by viewModels<QuizViewModel>()
        binding.apply {
            tvScore.text = "${args.score}/100"
            btnCongratzBack.setOnClickListener {
                findNavController().navigate(R.id.action_congratzFragment_to_coursesFragment)
            }
            btnSaveScore.setOnClickListener {
                //save score to firebase
                val newScore = args.score
                viewModel.saveScoreToFirebase(requireActivity(), auth,newScore,db,requireParentFragment())
            }

        }
    }


}