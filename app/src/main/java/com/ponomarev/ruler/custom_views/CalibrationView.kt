package com.ponomarev.ruler.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CalibrationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var measureCallback: (Int) -> Unit = { }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (h > 0) {
            measureCallback(h)
        }
    }

}
