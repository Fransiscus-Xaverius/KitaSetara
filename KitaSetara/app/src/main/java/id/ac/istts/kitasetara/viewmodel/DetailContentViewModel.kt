package id.ac.istts.kitasetara.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.model.course.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailContentViewModel: ViewModel() {
    private val coursesRepository:DefaultCoursesRepository = KitaSetaraApplication.coursesRepository
    private val _content = MutableLiveData<Content>()
    private val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)
    val content:LiveData<Content>
        get() = _content
    fun getContent(idContent:Int){
        ioScope.launch {
            _content.postValue(coursesRepository.getContent(idContent))
        }
    }
}