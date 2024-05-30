package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.ac.istts.kitasetara.databinding.FragmentNewPasswordBinding
import id.ac.istts.kitasetara.viewmodel.PasswordViewModel


class NewPasswordFragment : Fragment() {
    private var _binding : FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    private val viewModel : PasswordViewModel by viewModels()
    //get email that is being passed through navargs
    private val args : NewPasswordFragmentArgs by navArgs<NewPasswordFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    viewModel.updatePassword(email,requireParentFragment(),view,pass)
                }else Toast.makeText(requireActivity(),"New password and confirmation password doesn't match!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireActivity(),"All inputs need to be filled!",Toast.LENGTH_SHORT).show()
            }
        }
    }

}