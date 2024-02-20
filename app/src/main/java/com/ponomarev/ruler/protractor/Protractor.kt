package com.ponomarev.ruler.protractor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ponomarev.ruler.protractor.CoordinatesUtils.getCartesian
import com.ponomarev.ruler.protractor.CoordinatesUtils.getPolar
import com.ponomarev.ruler.protractor.CoordinatesUtils.getPolarWithDegrees
import com.ponomarev.ruler.TAG
import kotlin.math.PI

class Protractor : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /*
    был вызов из мэйна
     protractor.setOnTouchListener { v, event ->
            //Log.e(TAG, "onTouch: =${event?.rawX} ${event?.rawY} ${event?.action}" )
            if (event==null) return@setOnTouchListener true

            protractor.drawToCoordinates(event.x,event.y,event.action)
            true
        }

     */




    private val oneDegreeInRadian: Float = (PI / 180f).toFloat()

    private fun degreeToRadians(degree: Float): Float = (degree * oneDegreeInRadian).toFloat()


    private var pixelsInMm = 0f
    private val centerOfCircle
        get() = Pair(width - 2 * pixelsInMm, height / 2f)

    private val currentAngle: Float  //в радианах
        get() = (currentAngleDegree * oneDegreeInRadian)

    private var currentAngleDegree: Float = 45f
        set(value) {
            field = value
            invalidate()
        }


    private val currentAngleFormatted
        get() = "${"%.1f".format(currentAngleDegree)}°"


    private val paintText = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 0f

    }

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 0f
    }
    private val paintWhite: Paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 0f
    }


    private val paintRed: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2f
    }

    private val paintThick = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }


    var calibrated10cmHeight = 0
        set(value) {
            field = value
            pixelsInMm = value / 100f
            paintText.textSize = 5 * pixelsInMm
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        val radius = width * 0.9f
        canvas.drawText(currentAngleFormatted, pixelsInMm * 2, pixelsInMm * 6f, paintText)
        canvas.drawCircle(centerOfCircle.first, centerOfCircle.second, radius, paint)
        canvas.drawLine(
            centerOfCircle.first - width,
            centerOfCircle.second,
            centerOfCircle.first,
            centerOfCircle.second,
            paint
        )
        canvas.drawLine(
            centerOfCircle.first,
            centerOfCircle.second - height / 2,
            centerOfCircle.first,
            centerOfCircle.second + height / 2,
            paintThick
        )
        canvas.drawRect(
            centerOfCircle.first + 1,
            centerOfCircle.second - height / 2,
            width.toFloat(),
            centerOfCircle.second + height / 2,
            paintWhite
        )
        canvas.drawRect(0f, 1f, centerOfCircle.first, height.toFloat(), paintThick)
        val endpoint = getPolarWithDegrees(width.toFloat(), currentAngleDegree)
        val endpointXY = getCartesian(endpoint)

        canvas.drawLine(
            centerOfCircle.first,
            centerOfCircle.second,
            centerOfCircle.first + endpointXY.first,
            centerOfCircle.second + endpointXY.second,
            paintRed
        )
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.e(TAG, "onLayout: =$bottom / $right")
    }


    fun drawToCoordinates(x: Float, y: Float, action: Int) {
        /*
        val zoneX = pixelsInMm * 20
        val zoneY = pixelsInMm * 15
        if (x < zoneX && y < zoneY && action == ACTION_UP) {
            showDialog()
            return
        }
        */

        val polars = getPolar(Pair(centerOfCircle.first - x, centerOfCircle.second - y))
        currentAngleDegree = ((PI - polars.phi) / oneDegreeInRadian).toFloat()
    }

    private fun showDialog() {
        Log.e(TAG, "showDialog: =")

    }


}
