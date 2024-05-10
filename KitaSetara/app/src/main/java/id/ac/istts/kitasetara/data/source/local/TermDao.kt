package id.ac.istts.kitasetara.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.kitasetara.model.term.Term

@Dao
interface TermDao {
    @Query("select * from terms")
    fun getAll():LiveData<List<Term>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Term>)

    //get random term to be displayed on home fragment
    @Query("SELECT * FROM terms ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTerm(): Term?
}