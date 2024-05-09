package id.ac.istts.kitasetara.data.source.remote

import id.ac.istts.kitasetara.model.quotes.Quote
import id.ac.istts.kitasetara.model.quotes.QuoteResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//to handle CRUD of remote data from API
interface QuoteService {
    //request ke API, kembalian berbentuk quoteResult
    @GET("quotes")
    suspend fun getAllQuotes(@Query("genre") genre:String="equality") : Response<QuoteResult>
}