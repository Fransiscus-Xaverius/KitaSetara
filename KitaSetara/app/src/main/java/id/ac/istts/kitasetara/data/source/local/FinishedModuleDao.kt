package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.istts.kitasetara.model.course.FinishedModule


@Dao
interface FinishedModuleDao {

    @Query("SELECT * FROM finished_modules")
    fun getAllFinishedModules():List<FinishedModule>

    @Query("DELETE FROM finished_modules")
    fun clearFinishedModules()

    @Insert
    fun insertMany(finishedModules:List<FinishedModule>)
}