package id.ac.istts.kitasetara.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ac.istts.kitasetara.model.quotes.Quote
import id.ac.istts.kitasetara.model.quotes.QuoteEntity

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): LiveData<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Insert
    suspend fun insertRandomQuote(quote: QuoteEntity)

}