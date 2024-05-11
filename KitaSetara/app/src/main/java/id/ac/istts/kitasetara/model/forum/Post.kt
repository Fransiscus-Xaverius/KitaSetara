package id.ac.istts.kitasetara.model.forum

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.Date

@JsonClass(generateAdapter = true)
data class Post(
    val id_post:String? = null,
    val title:String? = null,
    val content:String? = null,
    val authorId:String? = null,
    val author:String? = null,
    val createdAt:String? = null,
    val updatedAt:String? = null,
    val lastComment:Comment? = null,
)
