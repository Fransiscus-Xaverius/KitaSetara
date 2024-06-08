package id.ac.istts.kitasetara.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.adapters.ChatbotAdapter
import id.ac.istts.kitasetara.databinding.FragmentChatbotBinding
import id.ac.istts.kitasetara.model.chatbot.Message
import id.ac.istts.kitasetara.viewmodel.ChatbotViewModel

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ChatbotViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun resetEt() {
        binding.etAskbot.setText("") //set empty initially
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetEt()

        binding.rvChatbot.layoutManager = LinearLayoutManager(requireActivity())
        val tempList = arrayListOf<Message>()
        tempList.add(Message("Hello there!", "bot"))
        val adapter = ChatbotAdapter(tempList)
        binding.rvChatbot.adapter = adapter

        binding.ivBackChatbot.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.btnSendchat.setOnClickListener {
            val message = binding.etAskbot.text.toString()

            //get response from AI
            val result = viewModel.sendMessage(message)
            if (result != "failed"){
                //success
                tempList.add(Message(message, "you"))
                resetEt()
                tempList.add(Message(result, "bot"))
                adapter.notifyDataSetChanged()
                val imm = context?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }else{
                Helper.showSnackbar(requireView(),"Try another message")
            }
        }
    }

}