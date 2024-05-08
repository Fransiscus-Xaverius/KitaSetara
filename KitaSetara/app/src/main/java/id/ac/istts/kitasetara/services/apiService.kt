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
import id.ac.istts.kitasetara.model.forum.newPost
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

public interface apiService {

    //USERS =================================================================================================


    //POSTS =================================================================================================
    @GET("posts") //Get all posts from API
    suspend fun getAllPosts(): List<Post>

    @GET("posts/{id}") //Get a post by id from API
    suspend fun getPostById(@Path("id") id: Int): Post

    @GET("posts/comments") //Get all comments of post from API
    suspend fun getAllComments(@Body id_post: String, type:Int): List<Comment>

    @POST("post") //Send new post to API
    suspend fun createPost(@Body newPost: newPost): Post

    //COMMENTS ==============================================================================================

}

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val msg:String
)

object API{
    val moshi = Moshi.Builder()
        .add(Date::class.java, object : JsonAdapter<Date>() {
            @FromJson
            override fun fromJson(reader: JsonReader): Date? {
                return if (reader.peek() != JsonReader.Token.NULL) {
                    Date(reader.nextLong())
                } else {
                    reader.nextNull<Any>()
                    null
                }
            }

            @ToJson
            override fun toJson(writer: JsonWriter, value: Date?) {
                value?.let { writer.value(it.time) }
            }
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://51.79.161.113:3000/api/") //API URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitService:apiService by lazy{
        retrofit.create(apiService::class.java)
    }
}