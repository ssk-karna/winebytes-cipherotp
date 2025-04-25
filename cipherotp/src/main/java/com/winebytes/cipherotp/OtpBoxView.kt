package com.winebytes.cipherotp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import android.util.TypedValue


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
                val marginInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics
                ).toInt()

                layoutParams = LayoutParams(marginInPx * 24, marginInPx * 24).apply {
                    if (index > 0 && index < otpLength - 1) {
                        setMargins(marginInPx, 0, marginInPx, 0)
                    } else if (index == 0) {
                        rightMargin = marginInPx
                    } else if (index == otpLength - 1) {
                        leftMargin = marginInPx
                    }
                }
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(1))
                textAlignment = TEXT_ALIGNMENT_CENTER
                background = ContextCompat.getDrawable(context, R.drawable.otp_box_background)
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

                addTextChangedListener(getOtpTextWatcher(index))
            }
            addView(editText)
            boxes.add(editText)
        }
    }

    fun getOtp(): String = boxes.joinToString("") { it.text.toString() }
    fun setOtp(value: String) {
        value.forEachIndexed { index, char ->
            if (index < boxes.size) boxes[index].setText(char.toString())
        }
    }

    private fun getOtpTextWatcher(index: Int): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && index < otpLength - 1) {
                    boxes[index + 1].requestFocus()
                }
            }
        }
    }
}