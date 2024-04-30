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
import id.ac.istts.kitasetara.databinding.FragmentLoginBinding
import id.ac.istts.kitasetara.model.User

class LoginFragment : Fragment() {
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
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle onclick
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
    }

    private fun loginUser(view: View, username : String, password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(User::class.java)

                        if (userData != null && userData.password == password){
                            //login successful
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