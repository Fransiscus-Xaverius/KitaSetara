package id.ac.istts.kitasetara.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailCourseFragmentViewModel:ViewModel() {
    private val coursesRepository = KitaSetaraApplication.coursesRepository
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)


    private var _course:MutableLiveData<Course> = MutableLiveData(Course(0, "", ""))
    private val _modules = MutableLiveData<List<Module>>(arrayListOf())

    val course: LiveData<Course>
        get() =_course

    fun getCourse(idCourse:Int){
        ioScope.launch {
            _course.postValue(coursesRepository.getCourseById(idCourse))
        }
    }

    val modules: LiveData<List<Module>>
        get() = _modules

    fun getCourseModules(idCourse: Int){
        ioScope.launch {
            _modules.postValue(coursesRepository.getAllCourseModule(idCourse))
        }
    }

}