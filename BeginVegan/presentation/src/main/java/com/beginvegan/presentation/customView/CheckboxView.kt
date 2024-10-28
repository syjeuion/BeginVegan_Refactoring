package com.beginvegan.presentation.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.beginvegan.presentation.R


class CheckboxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var onCheckedChangeListener: OnCheckedChangeListener? = null


    private val checkBoxImage: ImageView
    private val checkBoxText: TextView
    var isChecked: Boolean = false
        set(value) {
            field = value
            updateCheckBoxImage()
            onCheckedChangeListener?.onCheckedChanged(this, value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view_checkbox, this, true)
        checkBoxImage = findViewById(R.id.customCheckBoxImage)
        checkBoxText = findViewById(R.id.customCheckBoxText)

        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomCheckboxView, 0, 0).apply {
            try {
                val text = getString(R.styleable.CustomCheckboxView_text) ?: ""
                isChecked = getBoolean(R.styleable.CustomCheckboxView_isChecked, false)
                setText(text)
            } finally {
                recycle()
            }
        }

        setOnClickListener {
            toggle()
        }

        updateCheckBoxImage()
    }

    private fun updateCheckBoxImage() {
        val drawableRes = if (isChecked) {
            R.drawable.ic_checkbox_on
        } else {
            R.drawable.ic_checkbox_off
        }
        checkBoxImage.setImageResource(drawableRes)
    }

    fun toggle() {
        isChecked = !isChecked
    }

    fun setText(text: String) {
        checkBoxText.text = text
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: CheckboxView, isChecked: Boolean)
    }


    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        onCheckedChangeListener = listener
    }
}