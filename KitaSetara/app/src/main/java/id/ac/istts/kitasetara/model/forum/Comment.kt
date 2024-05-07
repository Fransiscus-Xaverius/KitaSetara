package id.ac.istts.kitasetara.model.forum

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class Comment(
    val id_comment:String? = null,
    val comment:String? = null,
    val username:String? = null,
)
