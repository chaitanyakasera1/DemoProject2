package com.example.demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LineAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        color = context.getColor(R.color.line_color) // Set the color of the line
        strokeWidth =
            context.resources.getDimension(R.dimen.line_thickness) // Set the thickness of the line
    }

    // Length of the line
    private var lineLength = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the line
        canvas.drawLine(0f, 0f, 0f, lineLength, paint)
    }

    // Method to set the length of the line
    fun setLineLength(length: Float) {
        lineLength = length
        invalidate() // Redraw the view
    }
}