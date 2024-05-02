package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.istts.kitasetara.model.course.Module

@Dao
interface ModuleDao {
    @Query("SELECT * FROM modules WHERE course_id = :id")
    fun getAllModulesByCourseId(id:Int):List<Module>

    @Query("SELECT * FROM modules WHERE id = :id")
    fun getModule(id:Int):Module


    @Query("DELETE FROM modules")
    fun clearModules()

    @Insert
    fun insertMany(modules:List<Module>)

}