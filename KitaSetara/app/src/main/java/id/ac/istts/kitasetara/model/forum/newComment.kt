package id.ac.istts.kitasetara.model.forum

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class newComment(
    val id_post:String,
    val username:String,
    val displayName: String,
    val comment:String
)
