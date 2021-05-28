package com.platform45.weather45.customViews

import android.content.Context
import android.util.AttributeSet

class MySpinner : androidx.appcompat.widget.AppCompatSpinner {
    var isInit: Boolean = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
    }

}