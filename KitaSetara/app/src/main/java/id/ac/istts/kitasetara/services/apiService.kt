package id.ac.istts.kitasetara.services

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import id.ac.istts.kitasetara.model.forum.Comment
import id.ac.istts.kitasetara.model.forum.Post
import id.ac.istts.kitasetara.model.forum.newComment
import id.ac.istts.kitasetara.model.forum.newPost
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

public interface apiService {

    //USERS =================================================================================================


    //POSTS =================================================================================================
    @GET("posts") //Get all posts from API
    suspend fun getAllPosts(): List<Post>

    @GET("post/{id}") //Get a post by id from API
    suspend fun getPostById(@Path("id") id: String): Post

    @GET("post/comments/{id}") //Get all comments of post from API
    suspend fun getAllComments(@Path("id") id: String): List<Comment>

    @POST("post") //Send new post to API
    suspend fun createPost(@Body newPost: newPost): Post

    //COMMENTS ==============================================================================================

    @POST("comment") //Send new comment to API
    suspend fun createComment(@Body comment: newComment): Comment

}

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val msg:String
)
class DateAdapter : JsonAdapter<Long>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Long? {
        val dateString = reader.nextString()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(dateString)
        return date?.time
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Long?) {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val dateString = format.format(Date(value ?: 0L))
        writer.value(dateString)
    }
}

object API{
    val moshi = Moshi.Builder()
        .add(Date::class.java, DateAdapter())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.79.161.113:3000/api/") //API URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitService:apiService by lazy{
        retrofit.create(apiService::class.java)
    }
}
