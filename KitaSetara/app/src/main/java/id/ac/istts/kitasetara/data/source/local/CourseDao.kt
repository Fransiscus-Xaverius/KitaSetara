package id.ac.istts.kitasetara.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.istts.kitasetara.model.course.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAll():List<Course>

    @Query("SELECT * FROM courses WHERE id=:id")
    fun getById(id:Int): Course

    @Query("DELETE FROM courses")
    fun clearCourses()

    @Insert
    fun insertMany(courses:List<Course>)
}