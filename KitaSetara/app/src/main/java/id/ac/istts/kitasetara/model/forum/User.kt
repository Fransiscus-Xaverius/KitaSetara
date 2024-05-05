package id.ac.istts.kitasetara.model.forum

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:String? = null,
    val username: String = "",
    val name : String = "",
    val password : String = "",
    val email : String = "",
    val isLoggedIn : Boolean = false,
    var imageUrl : String? = ""
) : Parcelable
