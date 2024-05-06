package id.ac.istts.kitasetara.data

import id.ac.istts.kitasetara.data.source.remote.QuoteService
import id.ac.istts.kitasetara.model.quotes.Quote

//sementara remote aja sourcenya
class DefaultQuotesRepository(
    private val remoteDataSource : QuoteService
) {
    suspend fun getSingleQuote() : Quote {
        return remoteDataSource.getAllQuotes().first()
    }
}