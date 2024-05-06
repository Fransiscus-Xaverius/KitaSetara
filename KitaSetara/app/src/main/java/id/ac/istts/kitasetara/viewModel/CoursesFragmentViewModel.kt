package id.ac.istts.kitasetara.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.data.DefaultCoursesRepository
import id.ac.istts.kitasetara.model.course.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoursesFragmentViewModel:ViewModel() {
    private val coursesRepository = KitaSetaraApplication.coursesRepository
    private val _courses = MutableLiveData<List<Course>>(listOf())
    private val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    val courses:LiveData<List<Course>>
        get() =_courses

    fun getCourses(){
        ioScope.launch {
            _courses.postValue(coursesRepository.getAllCourses())
        }
    }


}