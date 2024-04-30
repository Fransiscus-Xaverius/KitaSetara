package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentRegisterBinding
import id.ac.istts.kitasetara.model.User
import android.util.Patterns

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseDatabase: FirebaseDatabase //Firebase instance
    private lateinit var databaseReference: DatabaseReference //to connect with DB
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    private fun signupUser(view: View, username: String, password: String, email: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        //there is no user with given username yet in the DB
                        val id = databaseReference.push().key //create unique id for new user
                        val userData = User(id, username, password, email, false)
                        //child() is filled with id!! because the id will always be unique
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(context, "Register success!!", Toast.LENGTH_SHORT).show()
                        view.findNavController()
                            .navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "Username already exists!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Database Error : ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        signupUser(view,username,pass,email)
                    }else{
                        Toast.makeText(context, "Incorrect email format!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else Toast.makeText(context, "Password and confirm password does not match!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "All inputs need to be filled!", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        binding.btnToLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}