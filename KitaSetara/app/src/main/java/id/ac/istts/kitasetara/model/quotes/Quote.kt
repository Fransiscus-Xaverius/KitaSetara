package id.ac.istts.kitasetara.model.quotes

import com.squareup.moshi.Json

data class Pagination(
    @Json(name = "currentPage") val currentPage: Int,
    @Json(name = "nextPage") val nextPage: Int,
    @Json(name = "totalPages") val totalPages: Int
)

data class Quote(
    @Json(name = "_id") val id: String,
    @Json(name = "quoteText") val quoteText: String,
    @Json(name = "quoteAuthor") val quoteAuthor: String,
    @Json(name = "quoteGenre") val quoteGenre: String,
    @Json(name = "__v") val v: Int
)

data class QuoteResult(
    @Json(name = "statusCode") val statusCode: Int,
    @Json(name = "message") val message: String,
    @Json(name = "pagination") val pagination: Pagination,
    @Json(name = "totalQuotes") val totalQuotes: Int,
    @Json(name = "data") val data: List<Quote>
)
