package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {
    private var _binding : FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : FirebaseDatabase //to access database
    private lateinit var usersRef: DatabaseReference //to access specific table (users)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance() //get db instance
        usersRef = database.getReference("users") //get all data from users table
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //handle onclick
        binding.btnContinue1.setOnClickListener {
            //navigate to next page
            //check input not empty beforehand
            val email = binding.etResetEmail.text.toString()
            if (email.isNotEmpty()){
                //check whether there is a user associated with this email address in firebase db
                validateEmail(email)
            }else Toast.makeText(requireActivity(),"Email can't be empty!",Toast.LENGTH_SHORT).show()
        }

        binding.ivBackbutton.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun validateEmail(email:String){
        val query = usersRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User exists
                    //pass email as a navargs to NewPasswordFragment
                    val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToNewPasswordFragment(email)
                    findNavController().navigate(action)
                } else {
                    // User with given email does not exist
                    Toast.makeText(requireActivity(),"Email not registered!",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}