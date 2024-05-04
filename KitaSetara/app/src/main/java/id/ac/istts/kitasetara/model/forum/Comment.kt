package id.ac.istts.kitasetara.model.forum

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class Comment(
    val id:String? = null,
    val content:String? = null,
    val author:String? = null,
)
