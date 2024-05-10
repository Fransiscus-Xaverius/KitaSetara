package id.ac.istts.kitasetara.data

import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module

class DefaultCoursesRepository(
    private val localDataSource:AppDatabase
) {
    fun getAllCourses():List<Course> {
        return localDataSource.courseDao().getAll()
    }

    fun getCourseById(id:Int): Course {
        return localDataSource.courseDao().getById(id)
    }

    fun getAllCourseModule(idCourse:Int):List<Module>{
        return  localDataSource.moduleDao().getAllModulesByCourseId(idCourse)
    }

    fun insertCourses(courses:List<Course>){
        localDataSource.courseDao().insertMany(courses)
    }

    fun clearAllCourses(){
        localDataSource.courseDao().clearCourses()
    }

    fun getModule(idModule:Int):Module{
        return  localDataSource.moduleDao().getModule(idModule)
    }

    fun getAllModuleContent(idModule:Int):List<Content>{
        return  localDataSource.contentDao().getAllContentByModuleId(idModule)
    }

    fun insertModules(modules:List<Module>){
        localDataSource.moduleDao().insertMany(modules)
    }

    fun clearModules(){
        localDataSource.moduleDao().clearModules()
    }

    fun getContent(idContent:Int):Content{
        return  localDataSource.contentDao().getContent(idContent)
    }

    fun insertContents(contents:List<Content>){
        localDataSource.contentDao().insertMany(contents)
    }

    fun clearContents(){
        localDataSource.contentDao().clearContents()
    }
}