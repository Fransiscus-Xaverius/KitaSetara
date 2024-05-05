package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentNewPasswordBinding


class NewPasswordFragment : Fragment() {
    private var _binding : FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    //get email that is being passed through navargs
    private val args : NewPasswordFragmentArgs by navArgs<NewPasswordFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle onclick
        binding.ivBackbutton2.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btnContinueSubmit.setOnClickListener {
            //check input not empty
            val pass = binding.etNewpass.text.toString()
            val conf = binding.etConfNewpass.text.toString()
            if (pass.isNotEmpty() && conf.isNotEmpty()){
                //check pass and confirmation pass match
                if (pass == conf){
                    //success
                    val email = args.email
                    //update user's password
                    val query = usersRef.orderByChild("email").equalTo(email)
                    query.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                val userId = snapshot.children.first().key //get user id
                                userId?.let {
                                    val userRef = usersRef.child(it) //user node
                                    userRef.child("password").setValue(pass)
                                        .addOnSuccessListener {
                                            // password updated successfully
                                            Snackbar.make(view,"Password has been reset successfully",Snackbar.LENGTH_SHORT).show()
                                            //redirect to login page
                                            findNavController().navigate(R.id.action_global_loginFragment)
                                        }
                                        .addOnFailureListener {
                                            // Failed to update password
                                            Snackbar.make(view,"Password reset failed!",Snackbar.LENGTH_SHORT).show()
                                        }
                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Snackbar.make(view,"Oops.. Something wrong happened",Snackbar.LENGTH_SHORT).show()
                        }

                    })
                }else Toast.makeText(requireActivity(),"New password and confirmation password doesn't match!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireActivity(),"All inputs need to be filled!",Toast.LENGTH_SHORT).show()
            }
        }
    }

}