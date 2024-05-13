package id.ac.istts.kitasetara.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.kitasetara.model.quiz.Question

@Dao
interface QuestionDao {
    @Query("select * from questions")
    fun getAll(): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Question>)
}