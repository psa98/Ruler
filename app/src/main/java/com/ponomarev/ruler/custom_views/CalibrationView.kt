package com.ponomarev.ruler.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CalibrationView : View {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var measureCallback: (Int)->Unit = {  }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        measureCallback(bottom)
    }

}
