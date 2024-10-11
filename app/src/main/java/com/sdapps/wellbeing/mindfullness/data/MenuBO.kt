package com.sdapps.wellbeing.mindfullness.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuBO(
    var mainMenu: String = "Breathe",
    var attr : Int = 15,
    var isTime: Boolean = true,
    var image: Bitmap? = null
): Parcelable