package id.ac.istts.kitasetara.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.BuildConfig
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentLoginBinding
import id.ac.istts.kitasetara.model.forum.User

class LoginFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
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

        val currentUser = auth.currentUser

        if (currentUser != null) {
            //user has logged in already, navigate to home fragment right away
            view.findNavController().navigate(R.id.action_global_homeFragment)
        }else{

        }

        //handle onclick

        binding.signInButton.setOnClickListener {
            signIn() //login with google
        }

        binding.btnLogin.setOnClickListener {
            //validate inputs
            val username = binding.etLoginUsername.text.toString()
            val pass = binding.etLoginPassword.text.toString()
            if (username.isNotEmpty() && pass.isNotEmpty()){
                loginUser(view,username,pass)
            }else Toast.makeText(context, "All inputs need to be filled!", Toast.LENGTH_SHORT)
                .show()
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
    private fun loginUser(view: View, username : String, password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(User::class.java)

                        if (userData != null && userData.password == password){
                            //login successful
                            //set currentuser
                            Helper.currentUser = userData
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT)
                                .show()
                            userSnapshot.ref.child("loggedIn").setValue(true)
                            //redirect to home fragment
                            view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }else Toast.makeText(context, "Username or password is incorrect!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(context, "Username or password is incorrect!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error : ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}