package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.ac.istts.kitasetara.databinding.FragmentResetPasswordBinding
import id.ac.istts.kitasetara.viewmodel.PasswordViewModel

class ResetPasswordFragment : Fragment() {
    private var _binding : FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel : PasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
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
                viewModel.validateEmail(email,requireActivity(),requireParentFragment())
            }else Toast.makeText(requireActivity(),"Email can't be empty!",Toast.LENGTH_SHORT).show()
        }

        binding.ivBackbutton.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}