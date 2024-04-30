package com.kitasetara.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id:String? = null,
    val content:String? = null,
    val author:String? = null,
):Parcelable
