package com.example.praktam2_2417051007.model
import android.content.Context

object WisataSource {
    fun getResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}