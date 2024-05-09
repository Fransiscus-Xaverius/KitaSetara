package id.ac.istts.kitasetara.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.istts.kitasetara.KitaSetaraApplication
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Module
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailModuleViewModel:ViewModel() {
    private val coursesRepository = KitaSetaraApplication.coursesRepository
    private val ioScope:CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var _module:MutableLiveData<Module> = MutableLiveData(Module(0, "", "", ""))
    private val _contents:MutableLiveData<List<Content>> = MutableLiveData(arrayListOf())

    val module:LiveData<Module>
        get() = _module

    fun getModule(idModule:Int){
       ioScope.launch{
           _module.postValue(coursesRepository.getModule(idModule))
       }
    }

    val contents:LiveData<List<Content>>
        get() = _contents

    fun getModuleContents(idModule: Int){
        ioScope.launch {
            _contents.postValue(coursesRepository.getAllModuleContent(idModule))
        }
    }

}