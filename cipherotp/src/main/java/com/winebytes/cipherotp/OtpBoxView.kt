package com.winebytes.cipherotp

import android.content.Context
import androidx.annotation.IntRange
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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
import com.winebytes.cipherotp.utils.Constants
import com.winebytes.cipherotp.utils.Extensions.Companion.dpToPx


class OtpBoxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var otpLength = Constants.MAX_OTP_LENGTH// Default length
    private val boxes = mutableListOf<EditText>()
    private var borderColor: Int = Color.BLACK // default

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4f
    }

    init {
        orientation = HORIZONTAL
        extractAttributes(attrs)
        setupOtpBoxes()
    }

    private fun extractAttributes(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.OtpBoxView, 0, 0).apply {
            try {
                borderColor = getColor(R.styleable.OtpBoxView_boxBorderColor, Color.BLACK)
            } finally {
                recycle()
            }
        }
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
                background = createBoxBackground(borderColor)
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


    private fun createBoxBackground(color: Int): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.TRANSPARENT)
            setStroke(2.dpToPx(context), color)
            cornerRadius = 6.dpToPx(context).toFloat()
        }
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        // Update each EditText with the new border color
        boxes.forEach { editText ->
            val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.otp_box_background)
            if (backgroundDrawable is GradientDrawable) {
                backgroundDrawable.setStroke(2.dpToPx(context), borderColor)
            }
            editText.background = backgroundDrawable
        }
        invalidate()  // To force a redraw with the new border color
    }

    fun getOtp(): String = boxes.joinToString("") { it.text.toString() }
    fun setOtp(value: String) {
        value.forEachIndexed { index, char ->
            if (index < boxes.size) boxes[index].setText(char.toString())
        }
    }

    // Expose a setter method to change the number of OTP boxes programmatically
    fun setOtpBoxCount(@IntRange(from = Constants.MIN_OTP_LENGTH.toLong(), to = Constants.MAX_OTP_LENGTH.toLong()) count: Int = Constants.MIN_OTP_LENGTH) { // Ensure it's between 4 and 8
        otpLength = count
        setupOtpBoxes()  // Rebuild the boxes with the new count
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