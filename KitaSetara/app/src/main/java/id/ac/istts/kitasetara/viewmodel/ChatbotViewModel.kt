package id.ac.istts.kitasetara.viewmodel

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import id.ac.istts.kitasetara.BuildConfig
import kotlinx.coroutines.runBlocking

class ChatbotViewModel : ViewModel() {
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

    fun sendMessage(message : String) : String {
        try {
            val prompt = message+". (Make your response short, no more than 2 paragraph)"
            val response = runBlocking {
                model.generateContent(prompt)
            }
            return response.text.toString() //success
        }catch (e:Exception){
            return "failed"
        }
    }
}