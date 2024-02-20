package com.ponomarev.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ponomarev.ruler.Ruler.Position.LEFT
import com.ponomarev.ruler.Ruler.Position.RIGHT

const val FIVE_MM_DASH = 5
const val TWO_MM_DASH = 2
const val CM_DASH = 5
const val TEXT_MARGIN = 5
const val TEXT_SIZE = 4

class Ruler : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var pixelsInMm = 0f
    private var rulerLen = 0f
    private var rulerInCm = 0f
    private var rulerInMm = 0f
    private var fullHeight = 0f
    private var mmLines = emptyList<Float>().toMutableList()
    var side: Position = LEFT
    var calParameter: Float = DataRepository.calParameter
        set(value) {
            field = value
            calibrated10cmHeight = calibrated10cmHeight
            invalidate()
        }

    enum class Position {
        LEFT,
        RIGHT
    }


    //выставим показатель сеттером
    var calibrated10cmHeight = 0
        set(value) {
            field = value
            pixelsInMm = (value / 100f) * calParameter
            if (fullHeight != 0f) calculateRulerParams()
        }

    // Вызывается если известны и высота калибровочного вью и текущего
    private fun calculateRulerParams() {
        mmLines.clear()
        rulerLen = fullHeight
        rulerInCm = rulerLen / (pixelsInMm * 10)
        rulerInMm = rulerLen / (pixelsInMm)
        for (lineNumber in rulerInMm.toInt() downTo 0) {
            mmLines.add((lineNumber * pixelsInMm))
        }
        blackPaint.textSize = TEXT_SIZE * pixelsInMm / calParameter
        invalidate()
    }

    private val blackPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 0f
    }

    private val whitePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 0f
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f, width.toFloat(), height.toFloat(),whitePaint)
        mmLines.forEachIndexed() { index, position ->
            val tickWidth = when {
                index % 5 == 0 && index % 10 != 0 -> pixelsInMm * FIVE_MM_DASH / calParameter
                index % 10 == 0 -> pixelsInMm * CM_DASH / calParameter
                else -> pixelsInMm * TWO_MM_DASH / calParameter
            }
            when (side) {
                LEFT -> canvas.drawLine(0f, position, tickWidth, position, blackPaint)
                RIGHT ->
                    canvas.drawLine(width - tickWidth, position, width.toFloat(),
                        position, blackPaint)
            }
            var text = (index / 10).toString()
            if (index / 10 < 10) text = "  $text"
            if (index % 10 == 0) {
                canvas.drawText(text, pixelsInMm * TEXT_MARGIN / calParameter,
                    position, blackPaint)
            }
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        fullHeight = bottom.toFloat()
        if (pixelsInMm != 0f) calculateRulerParams()
    }


}
