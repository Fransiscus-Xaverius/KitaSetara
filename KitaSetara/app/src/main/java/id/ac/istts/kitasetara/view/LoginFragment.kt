package id.ac.istts.kitasetara.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.ac.istts.kitasetara.BuildConfig
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentLoginBinding
import id.ac.istts.kitasetara.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentLoginBinding.inflate(inflater, container, false)
        //get firebaseDatabase instance
        firebaseDatabase = FirebaseDatabase.getInstance()
        //make database table with the name "users"
        databaseReference = firebaseDatabase.reference.child("users")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        playAnimation()
        //handle onclick

        binding.signInButton.setOnClickListener {
            signIn() //login with google
        }

        binding.btnLogin.setOnClickListener {
            //validate inputs
            val username = binding.etLoginUsername.text.toString()
            val pass = binding.etLoginPassword.text.toString()
            if (username.isNotEmpty() && pass.isNotEmpty()){
                viewModel.loginUser(view,username,pass,firebaseDatabase,databaseReference,requireActivity())
            }else Helper.showSnackbar(view, "All inputs need to be filled!")

        }
        binding.tvCreateAcc.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.tvForgot.setOnClickListener {
            //handle forgot password
            //redirect to reset password fragment
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA,1f).setDuration(200)
        val iv = ObjectAnimator.ofFloat(binding.imageView,View.ALPHA,1f).setDuration(200)
        val btnGoogle = ObjectAnimator.ofFloat(binding.signInButton,View.ALPHA,1f).setDuration(200)
        val icGoogle = ObjectAnimator.ofFloat(binding.iconGoogle,View.ALPHA,1f).setDuration(200)
        val linearGoogle = ObjectAnimator.ofFloat(binding.linearLayout,View.ALPHA,1f).setDuration(200)
        val tvOr = ObjectAnimator.ofFloat(binding.textView7,View.ALPHA,1f).setDuration(200)
        val tvUsername = ObjectAnimator.ofFloat(binding.textInputLayout4,View.ALPHA,1f).setDuration(200)
        val tvPass = ObjectAnimator.ofFloat(binding.textInputLayout5,View.ALPHA,1f).setDuration(200)
        val tvForgot = ObjectAnimator.ofFloat(binding.tvForgot,View.ALPHA,1f).setDuration(200)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin,View.ALPHA,1f).setDuration(200)
        val tvNr = ObjectAnimator.ofFloat(binding.textView3,View.ALPHA,1f).setDuration(200)
        val tvCreate = ObjectAnimator.ofFloat(binding.tvCreateAcc,View.ALPHA,1f).setDuration(200)

        val together = AnimatorSet().apply {
            playTogether(btnGoogle,icGoogle,linearGoogle, tvOr,tvUsername,tvPass)
        }

        val together2 = AnimatorSet().apply {
            playTogether(tvNr, tvCreate)
        }
        //start animation
        AnimatorSet().apply {
            playSequentially(title,iv,together,tvForgot,btnLogin, together2)
            start()
        }
    }

    private fun signIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireActivity(), "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireActivity(), "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    //redirect to home activity
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


}