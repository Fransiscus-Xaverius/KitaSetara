package id.ac.istts.kitasetara.model.forum

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class newPost(
    val title: String,
    val content: String,
    val authorName: String,
    val uid: String
)
