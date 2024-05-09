package id.ac.istts.kitasetara.data

import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.model.course.Content
import id.ac.istts.kitasetara.model.course.Course
import id.ac.istts.kitasetara.model.course.Module

class DefaultCoursesRepository(
    private val localDataSource:AppDatabase
) {
    suspend fun getAllCourses():List<Course> {
        return localDataSource.courseDao().getAll()
    }

    suspend fun getCourseById(id:Int): Course {
        return localDataSource.courseDao().getById(id)
    }

    suspend fun getAllCourseModule(idCourse:Int):List<Module>{
        return  localDataSource.moduleDao().getAllModulesByCourseId(idCourse)
    }

    suspend fun insertCourses(courses:List<Course>){
        localDataSource.courseDao().insertMany(courses)
    }

    suspend fun clearAllCourses(){
        localDataSource.courseDao().clearCourses()
    }

    suspend fun getModule(idModule:Int):Module{
        return  localDataSource.moduleDao().getModule(idModule)
    }

    suspend fun getAllModuleContent(idModule:Int):List<Content>{
        return  localDataSource.contentDao().getAllContentByModuleId(idModule)
    }

    suspend fun insertModules(modules:List<Module>){
        localDataSource.moduleDao().insertMany(modules)
    }

    suspend fun clearModules(){
        localDataSource.moduleDao().clearModules()
    }

    suspend fun getContent(idContent:Int):Content{
        return  localDataSource.contentDao().getContent(idContent)
    }

    suspend fun insertContents(contents:List<Content>){
        localDataSource.contentDao().insertMany(contents)
    }

    suspend fun clearContents(){
        localDataSource.contentDao().clearContents()
    }

}