package com.winebytes.cipherotp.utils

import android.content.Context

class Extensions {

    companion object {
        fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()

    }
}