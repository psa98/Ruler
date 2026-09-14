package com.ponomarev.ruler.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import com.ponomarev.ruler.R
import com.ponomarev.ruler.data.DataRepository

class Ruler @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class Position(val id: Int) {
        LEFT(0), RIGHT(1);

        companion object {
            fun fromId(id: Int): Position = Position.entries.find { it.id == id } ?: LEFT
        }
    }

    private var dashColor = 0
    private var rulerBackgroundColor = 0
    private var pixelsInMm = 0f
    
    var side: Position = Position.LEFT
        set(value) {
            field = value
            invalidate()
        }

    var calParameter: Float = DataRepository.getInstance(context).calParameter
        set(value) {
            field = value
            updatePixelsInMm()
            invalidate()
        }

    var calibrated10cmSize = 0
        set(value) {
            field = value
            updatePixelsInMm()
            invalidate()
        }

    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 0f // Hairline
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    init {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val defaultDashColor = typedValue.data
        context.theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        val defaultBgColor = typedValue.data

        context.withStyledAttributes(attrs, R.styleable.Ruler, defStyleAttr, 0) {
            side = Position.fromId(getInt(R.styleable.Ruler_side, Position.LEFT.id))
            dashColor = getColor(R.styleable.Ruler_dashColor, defaultDashColor)
            rulerBackgroundColor = getColor(R.styleable.Ruler_rulerBackgroundColor, defaultBgColor)
        }

        dashPaint.color = dashColor
        textPaint.color = dashColor
        backgroundPaint.color = rulerBackgroundColor
    }

    private fun updatePixelsInMm() {
        pixelsInMm = (calibrated10cmSize / 100f) * calParameter
        textPaint.textSize = TEXT_SIZE_SP * pixelsInMm / calParameter
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (pixelsInMm == 0f) updatePixelsInMm()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        if (pixelsInMm <= 0f) return

        val rulerInMm = height / pixelsInMm
        val textMarginPx = TEXT_MARGIN_MM * pixelsInMm / calParameter
        val topMarginPx = TOP_MARGIN_MM * pixelsInMm / calParameter
        for (mm in 0..rulerInMm.toInt()) {
            val y = mm * pixelsInMm +topMarginPx
            val isCm = mm % 10 == 0
            val isHalfCm = mm % 5 == 0 && !isCm

            val tickWidthMm = when {
                isCm -> CM_DASH_MM
                isHalfCm -> FIVE_MM_DASH_MM
                else -> TWO_MM_DASH_MM
            }
            val tickWidthPx = tickWidthMm * pixelsInMm / calParameter

            when (side) {
                Position.LEFT -> {
                    canvas.drawLine(0f, y, tickWidthPx, y, dashPaint)
                    if (isCm) {
                        val text = (mm / 10).toString()
                        canvas.drawText(text, textMarginPx, y + (textPaint.textSize / 3), textPaint)
                    }
                }
                Position.RIGHT -> {
                    canvas.drawLine(width - tickWidthPx, y, width.toFloat(), y, dashPaint)
                    if (isCm) {
                        val text = (mm / 10).toString()
                        val textWidth = textPaint.measureText(text)
                        canvas.drawText(text, width - textMarginPx - textWidth, y + (textPaint.textSize / 3), textPaint)
                    }
                }
            }
        }
    }

    companion object {
        private const val FIVE_MM_DASH_MM = 5f
        private const val TWO_MM_DASH_MM = 2f
        private const val CM_DASH_MM = 8f
        private const val TEXT_MARGIN_MM = 10f
        private const val TOP_MARGIN_MM = 2f
        private const val TEXT_SIZE_SP = 4f
    }
}
