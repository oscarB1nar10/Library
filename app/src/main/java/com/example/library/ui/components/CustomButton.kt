package com.example.library.ui.components

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.library.R

class CustomButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {

    companion object {
        const val STYLE_PRIMARY = 0
        const val STYLE_SECONDARY = 1
        const val STYLE_TERTIARY = 2
        const val STYLE_POSITIVE = 3
        const val STYLE_NEGATIVE = 4
        const val STYLE_CANCEL = 5
        const val STYLE_KEYBOARD = 6
        const val STYLE_STICKY = 7
    }

    var style: Int = STYLE_PRIMARY
        set(value) {
            field = value
            setStyle()
        }

    /**
     * Determines if we consider the field enabled. We can't use isEnabled because we still need to listen for click events.
     */
    var enable: Boolean = true
        set(value) {
            field = value
            setStyle()
        }

    init {
        setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (style == STYLE_TERTIARY || style == STYLE_POSITIVE || style == STYLE_NEGATIVE)
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                    (background as TransitionDrawable).startTransition(100)
                }
                MotionEvent.ACTION_UP -> {
                    if (style == STYLE_TERTIARY || style == STYLE_POSITIVE || style == STYLE_NEGATIVE)
                        setStyle()
                    (background as TransitionDrawable).reverseTransition(100)
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (style == STYLE_TERTIARY || style == STYLE_POSITIVE || style == STYLE_NEGATIVE)
                        setStyle()
                    (background as TransitionDrawable).resetTransition()
                }
            }
            // Return false to propagate the event to default touch listener
            false
        }

        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)

        style = attributeArray.getInt(R.styleable.CustomButton_style, STYLE_PRIMARY)
        enable = attributeArray.getBoolean(R.styleable.CustomButton_enable, true)

        setStyle()

        attributeArray.recycle()
    }

    private fun setStyle() {
        if (!enable) {
            if (style == STYLE_KEYBOARD || style == STYLE_STICKY)
                setBackgroundResource(R.drawable.disable_sticky_button_drawable)
            else
                setBackgroundResource(R.drawable.disable_button_drawable)
            setTextColor(ContextCompat.getColor(context, R.color.slate_300))
        } else {
            when (style) {

                STYLE_CANCEL -> {
                    setBackgroundResource(R.drawable.cancel_button_drawable)
                    setTextColor(ContextCompat.getColor(context, R.color.cancel_button_text_color))
                }
                STYLE_KEYBOARD -> {
                    setBackgroundResource(R.drawable.primary_sticky_button_drawable)
                    setTextColor(ContextCompat.getColor(context, R.color.primary_button_text_color))
                }
                STYLE_STICKY -> {
                    setBackgroundResource(R.drawable.primary_sticky_button_drawable)
                    setTextColor(ContextCompat.getColor(context, R.color.primary_button_text_color))
                }
            }
        }
    }
}