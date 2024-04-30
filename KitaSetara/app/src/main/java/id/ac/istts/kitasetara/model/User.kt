package id.ac.istts.kitasetara.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:String? = null,
    val username: String = "",
    val password : String = "",
    val email : String = "",
    val isLoggedIn : Boolean = false
) : Parcelable
