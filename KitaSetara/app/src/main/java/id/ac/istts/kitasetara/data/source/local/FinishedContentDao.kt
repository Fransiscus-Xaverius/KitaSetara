package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.kitasetara.model.course.FinishedContent


@Dao
interface FinishedContentDao {
    @Query("SELECT * FROM finished_contents")
    fun getAllFinishedContents():List<FinishedContent>

    @Query("DELETE FROM finished_contents")
    fun clearFinishedContents()

    @Insert
    fun insert(finishedContent: FinishedContent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(finishedContents: List<FinishedContent>)
}