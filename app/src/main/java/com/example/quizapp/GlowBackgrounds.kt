package com.example.quizapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

/**
 * Tła przycisków z poświatą przy wciśnięciu (wariant C z projektu):
 * rdzeń zostaje pełnowymiarowy, a wokół rysowany jest miękki halo
 * z koncentrycznych, gasnących pierścieni. Wymaga clipChildren=false
 * na rodzicu, żeby poświata mogła wyjść poza obrys przycisku.
 */
object GlowBackgrounds {

    fun quizAnswer(context: Context): Drawable {
        val d = context.resources.displayMetrics.density
        return StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                GlowPillDrawable(
                    radius = 24 * d,
                    fillColor = 0x663A2C7A,
                    strokeColor = 0xFFFFD98A.toInt(),
                    strokeWidth = 2 * d,
                    glowColor = 0xFFF5B04C.toInt(),
                    glowSize = 10 * d
                )
            )
            addState(intArrayOf(), pill(24 * d, 0x663A2C7A, 0xFFF5B04C.toInt(), 2 * d))
        }
    }

    fun trash(context: Context): Drawable {
        val d = context.resources.displayMetrics.density
        return StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                GlowPillDrawable(
                    radius = 11 * d,
                    fillColor = 0x33FF4444,
                    strokeColor = 0xFFFF8A80.toInt(),
                    strokeWidth = 1.5f * d,
                    glowColor = 0xFFEF5350.toInt(),
                    glowSize = 7 * d
                )
            )
            addState(intArrayOf(), pill(11 * d, 0x24FF4444, 0xFFFF4444.toInt(), 1 * d))
        }
    }

    private fun pill(radius: Float, fill: Int, stroke: Int, strokeWidth: Float): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = radius
            setColor(fill)
            setStroke(strokeWidth.toInt(), stroke)
        }
}

class GlowPillDrawable(
    private val radius: Float,
    fillColor: Int,
    strokeColor: Int,
    private val strokeWidth: Float,
    private val glowColor: Int,
    private val glowSize: Float
) : Drawable() {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = fillColor
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = strokeColor
        strokeWidth = this@GlowPillDrawable.strokeWidth
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        val core = RectF(bounds)

        // halo: gasnące pierścienie wychodzące poza obrys
        val steps = 10
        val step = glowSize / steps
        glowPaint.color = glowColor
        glowPaint.strokeWidth = step * 2f
        for (i in 0 until steps) {
            val expand = step * (i + 1)
            glowPaint.alpha = (70 * (1f - i.toFloat() / steps)).toInt()
            val ring = RectF(core)
            ring.inset(-expand, -expand)
            canvas.drawRoundRect(ring, radius + expand, radius + expand, glowPaint)
        }

        // rdzeń
        canvas.drawRoundRect(core, radius, radius, fillPaint)
        val inner = RectF(core)
        inner.inset(strokeWidth / 2f, strokeWidth / 2f)
        canvas.drawRoundRect(inner, radius - strokeWidth / 2f, radius - strokeWidth / 2f, strokePaint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}
