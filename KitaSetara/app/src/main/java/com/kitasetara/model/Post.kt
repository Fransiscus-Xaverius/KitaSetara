package com.kitasetara.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Post(
    val id:String? = null,
    val title:String? = null,
    val content:String? = null,
    val authorId:String? = null,
    val authorName:String? = null,
    val date:Date? = null,
):Parcelable
