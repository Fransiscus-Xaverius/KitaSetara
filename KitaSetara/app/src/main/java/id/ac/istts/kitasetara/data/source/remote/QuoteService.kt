package id.ac.istts.kitasetara.data.source.remote

import id.ac.istts.kitasetara.model.quotes.Quote
import retrofit2.http.GET
import retrofit2.http.Query

//to handle CRUD of remote data from API
interface QuoteService {
    @GET("quotes")
    suspend fun getAllQuotes(@Query("genre") genre:String="equality") : List<Quote>
}