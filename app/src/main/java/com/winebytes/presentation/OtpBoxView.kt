package com.winebytes.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.children

class OtpBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var otpLength = 4 // Default length
    private val boxes = mutableListOf<EditText>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4f
    }

    init {
        orientation = HORIZONTAL
        setupOtpBoxes()
    }

    private fun setupOtpBoxes() {
        removeAllViews()
        boxes.clear()
        repeat(otpLength) { index ->
            val editText = EditText(context).apply {
                layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(1))
                textAlignment = TEXT_ALIGNMENT_CENTER
                setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && text.isEmpty() && index > 0) {
                        boxes[index - 1].requestFocus()
                    }
                    false
                }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        clearFocus()
                    }
                    false
                }
                setOnFocusChangeListener { _, hasFocus -> invalidate() }
            }
            addView(editText)
            boxes.add(editText)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        children.forEach { view ->
            canvas.drawRect(view.left.toFloat(), view.top.toFloat(), view.right.toFloat(), view.bottom.toFloat(), paint)
        }
    }

    fun getOtp(): String = boxes.joinToString("") { it.text.toString() }
    fun setOtp(value: String) {
        value.forEachIndexed { index, char ->
            if (index < boxes.size) boxes[index].setText(char.toString())
        }
    }
}
