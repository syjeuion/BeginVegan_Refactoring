package com.beginvegan.presentation.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.beginvegan.presentation.R

class ToggleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var check: Boolean = false
        set(value) {
            field = value
            updateView()
            onCheckedChangeListener?.invoke(value)
        }

    private var textChecked: String = "--닫기"
    private var textUnchecked: String = "--더보기"
    private var iconCheckedResId: Int = R.drawable.ic_toggle_up
    private var iconUncheckedResId: Int = R.drawable.ic_toggle_down

    private var onCheckedChangeListener: ((Boolean) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view_toggle, this, true)

        orientation = HORIZONTAL

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ToggleView, 0, 0)
            check = typedArray.getBoolean(R.styleable.ToggleView_check, false)
            textChecked = typedArray.getString(R.styleable.ToggleView_text_checked) ?: textChecked
            textUnchecked =
                typedArray.getString(R.styleable.ToggleView_text_unchecked) ?: textUnchecked
            iconCheckedResId =
                typedArray.getResourceId(R.styleable.ToggleView_icon_checked, iconCheckedResId)
            iconUncheckedResId =
                typedArray.getResourceId(R.styleable.ToggleView_icon_unchecked, iconUncheckedResId)
            typedArray.recycle()
        }

        updateView()

        setOnClickListener {
            toggle()
        }
    }

    private fun updateView() {
        val textView = findViewById<TextView>(R.id.tv_toggle_text)
        val imageView = findViewById<ImageView>(R.id.iv_toggle_icon)

        if (check) {
            textView.text = textChecked
            imageView.setImageDrawable(ContextCompat.getDrawable(context, iconCheckedResId))
        } else {
            textView.text = textUnchecked
            imageView.setImageDrawable(ContextCompat.getDrawable(context, iconUncheckedResId))
        }
    }

    fun toggle() {
        check = !check
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }
}