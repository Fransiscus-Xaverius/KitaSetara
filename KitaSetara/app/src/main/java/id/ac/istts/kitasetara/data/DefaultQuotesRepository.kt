package id.ac.istts.kitasetara.data

import id.ac.istts.kitasetara.data.source.remote.QuoteService
import id.ac.istts.kitasetara.model.quotes.Quote

//sementara remote aja sourcenya
class DefaultQuotesRepository(
    private val remoteDataSource: QuoteService //terima data dari QuoteService
) {
    suspend fun getAllQuotes(genre: String = "equality"): Result<List<Quote>> {
        return try {
            val response = remoteDataSource.getAllQuotes(genre)
            if (response.isSuccessful) {
                val quotes = response.body()?.data ?: emptyList()
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


}