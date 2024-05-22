package id.ac.istts.kitasetara.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.model.quiz.Question
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val questionsRepository = KitaSetaraApplication.questionRepository
    //get all questions from ROOM
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    private val _questionNumber = MutableLiveData<Int>(1)
    val questionNumber : LiveData<Int> get() = _questionNumber

    private val _startIndex = MutableLiveData(0)
    val startIndex : LiveData<Int> get() = _startIndex

    init { //fetch automatically from firebase when this class is created
        fetchQuestions()
    }

    fun updateQNumber(){
        _questionNumber.value = _questionNumber.value!! + 1
    }
    fun initSetup() {// populate ROOM with data taken from firebase
        viewModelScope.launch {
            questionsRepository.fetchQuestionsAndSaveToLocalDatabase()
        }
    }

    fun setIndex(){
        _startIndex.value = _startIndex.value!! +1
    }
    fun getIndex():Int{
        return startIndex.value!!
    }

    fun getQuestions() : List<Question>{
        return questions.value!!
    }
    fun fetchQuestions() { //get data from repository to be displayed
        viewModelScope.launch {
            // Fetch questions from repository
            val fetchedQuestions = questionsRepository.getAllQuestions()
            // Update the value of _questions LiveData
            _questions.value = fetchedQuestions
        }
    }

    fun saveScoreToFirebase(context:Context, auth: FirebaseAuth,newScore:Int,db:FirebaseDatabase,fragment: Fragment) = questionsRepository.saveScoreToFirebase(context,auth,newScore,db,fragment)

}