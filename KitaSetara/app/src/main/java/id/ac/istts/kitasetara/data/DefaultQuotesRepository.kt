package id.ac.istts.kitasetara.data

import androidx.lifecycle.LiveData
import id.ac.istts.kitasetara.data.source.local.AppDatabase
import id.ac.istts.kitasetara.data.source.remote.QuoteService
import id.ac.istts.kitasetara.model.quotes.Quote
import id.ac.istts.kitasetara.model.quotes.QuoteEntity

//sementara remote aja sourcenya
class DefaultQuotesRepository(
    private val remoteDataSource: QuoteService, //terima data dari QuoteService
    private val localDataSource : AppDatabase
) {
    suspend fun getAllQuotes(genre: String = "equality"): Result<List<Quote>> {
        return try {
            val response = remoteDataSource.getAllQuotes(genre)
            if (response.isSuccessful) {
                val quotes = response.body()?.data ?: emptyList()
                localDataSource.quoteDao().insertQuotes(quotes.map {
                    it.toEntity()
                })
                Result.success(quotes)
            } else {
                Result.failure(Exception("Failed to fetch quotes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getRandomQuote(genre: String = "equality"): Result<Quote> {
        return try {
            val response = remoteDataSource.getAllQuotes(genre)
            if (response.isSuccessful) {
                val quotes = response.body()?.data ?: emptyList() //cek data response tidak null
                if (quotes.isNotEmpty()) {
                    val randomQuote = quotes.random()
                    //insert to local
                    localDataSource.quoteDao().insertRandomQuote(randomQuote.toEntity())
                    Result.success(randomQuote)
                }else {
                    Result.failure(Exception("No quotes found"))
                }
            } else {
                Result.failure(Exception("Failed to fetch quotes"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("gagal"))
        }
    }

    //function to get quotes from ROOM/local
    fun getQuotesFromLocal() : LiveData<List<QuoteEntity>>{
        return localDataSource.quoteDao().getAllQuotes()
    }
}