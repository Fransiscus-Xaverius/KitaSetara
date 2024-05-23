package id.ac.istts.kitasetara.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import id.ac.istts.kitasetara.BuildConfig
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.adapters.ChatbotAdapter
import id.ac.istts.kitasetara.databinding.FragmentChatbotBinding
import id.ac.istts.kitasetara.model.chatbot.Message
import kotlinx.coroutines.runBlocking

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun resetEt() {
        binding.etAskbot.setText("") //set empty initially
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetEt()
        val model = GenerativeModel(
            "gemini-1.5-flash-latest",
            // Retrieve API key as an environmental variable defined in a Build Configuration
            // see https://github.com/google/secrets-gradle-plugin for further instructions
            BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 2f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
            )
        )
        binding.rvChatbot.layoutManager = LinearLayoutManager(requireActivity())
        val tempList = arrayListOf<Message>()
        tempList.add(Message("Hello there!", "bot"))
        val adapter = ChatbotAdapter(tempList)
        binding.rvChatbot.adapter = adapter

        binding.ivBackChatbot.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.btnSendchat.setOnClickListener {
            val message = binding.etAskbot.text.toString()

            //get response from AI
            try {
                val prompt = message+". (Make your response short, no more than 1 paragraph)"
                binding.btnSendchat.isEnabled = false
                val response = runBlocking {
                    model.generateContent(prompt)
                }
                tempList.add(Message(message, "you"))
                resetEt()
                tempList.add(Message(response.text.toString(), "bot"))
                adapter.notifyDataSetChanged()
                binding.btnSendchat.isEnabled = true
            }catch (e:Exception){
                Helper.showSnackbar(requireView(),"Try another message")
                binding.btnSendchat.isEnabled = true
            }
        }
    }

}