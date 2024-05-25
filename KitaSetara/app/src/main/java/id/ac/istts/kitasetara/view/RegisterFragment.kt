package id.ac.istts.kitasetara.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.databinding.FragmentRegisterBinding
import id.ac.istts.kitasetara.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseDatabase: FirebaseDatabase //Firebase instance
    private lateinit var databaseReference: DatabaseReference //to connect with DB
    private val viewModel : RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        return binding.root

    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playAnimation()

        binding.btnRegister.setOnClickListener {
            //validate input
            val username = binding.etRegisUsername.text.toString()
            val name = binding.etRegisName.text.toString()
            val email = binding.etRegisEmail.text.toString()
            val pass = binding.etRegisPassword.text.toString()
            val conf = binding.etRegisConf.text.toString()
            if (username != "" && name != "" && email !="" && pass !="" && conf != "") {
                if (pass == conf) {
                    //validate email format
                    if (isValidEmail(email)){
                        viewModel.signupUser(view,username,name,pass,email,databaseReference,requireActivity())
                    }else{
                        Helper.showSnackbar(view, "Incorrect email format!")
                    }
                } else Helper.showSnackbar(view, "Password and confirm password does not match!")
            } else {
                Helper.showSnackbar(view, "All inputs need to be filled!")
            }

        }
        binding.btnToLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA,1f).setDuration(200)
        val iv = ObjectAnimator.ofFloat(binding.imageView2,View.ALPHA,1f).setDuration(200)
        val etName = ObjectAnimator.ofFloat(binding.til1,View.ALPHA,1f).setDuration(200)
        val etUsername = ObjectAnimator.ofFloat(binding.textInputLayout8,View.ALPHA,1f).setDuration(200)
        val etPass = ObjectAnimator.ofFloat(binding.textInputLayout7,View.ALPHA,1f).setDuration(200)
        val etConf = ObjectAnimator.ofFloat(binding.textInputLayout6,View.ALPHA,1f).setDuration(200)
        val etEmail = ObjectAnimator.ofFloat(binding.textInputLayout5,View.ALPHA,1f).setDuration(200)
        val btnRegis = ObjectAnimator.ofFloat(binding.btnRegister,View.ALPHA,1f).setDuration(200)
        val btnToLogin = ObjectAnimator.ofFloat(binding.btnToLogin,View.ALPHA,1f).setDuration(200)

        val together = AnimatorSet().apply {
            playTogether(btnRegis,btnToLogin)
        }

        val together2 = AnimatorSet().apply {
            playTogether(etName,etUsername,etPass,etConf,etEmail)
        }
        //start animation
        AnimatorSet().apply {
            playSequentially(title,iv,together2,together)
            start()
        }
    }
}