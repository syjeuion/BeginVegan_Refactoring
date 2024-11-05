package com.beginvegan.presentation.util

import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentManager
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseDialogFragment
import com.beginvegan.presentation.databinding.DialogNoTitleBinding

class NoTitleDialog private constructor(
    private val body: String?,
    private val positiveButtonText: String?,
    private val negativeButtonText: String?,
    private val positiveButtonListener: (() -> Unit)?,
    private val negativeButtonListener: (() -> Unit)?,
    private val onDismissListener: (() -> Unit)?
) : BaseDialogFragment<DialogNoTitleBinding>(DialogNoTitleBinding::inflate) {


    private fun setPositiveButton(text: String, listener: () -> Unit) {
        binding.btnApply.text = text.replace(" ", "\u00A0")
        binding.btnApply.setOnClickListener {
            listener()
            dismiss()
        }
    }

    private fun setNegativeButton(text: String, listener: () -> Unit) {
        binding.btnCancel.text = text.replace(" ", "\u00A0")
        binding.btnCancel.setOnClickListener {
            listener()
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    private fun setBody(body: String) {
        binding.tvBody.text = body
    }

    override fun init() {
        body?.let { setBody(it) }
        if (positiveButtonText != null && positiveButtonListener != null) {
            setPositiveButton(positiveButtonText, positiveButtonListener)
        } else {
            binding.btnApply.visibility = View.GONE
        }

        if (negativeButtonText != null && negativeButtonListener != null) {
            setNegativeButton(negativeButtonText, negativeButtonListener)
        } else {
            binding.btnCancel.visibility = View.GONE
        }
    }

    class Builder {
        private var title: String? = null
        private var body: String? = null
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var positiveButtonListener: (() -> Unit)? = null
        private var negativeButtonListener: (() -> Unit)? = null
        private var onDismissListener: (() -> Unit)? = null

        fun setTitle(title: String) = apply { this.title = title.replace(" ", "\u00A0") }
        fun setBody(body: String) = apply { this.body = body.replace(" ", "\u00A0") }

        fun setPositiveButton(text: String, listener: () -> Unit) = apply {
            this.positiveButtonText = text
            this.positiveButtonListener = listener
        }

        fun setNegativeButton(text: String, listener: () -> Unit) = apply {
            this.negativeButtonText = text
            this.negativeButtonListener = listener
        }

        fun setOnDismissListener(listener: () -> Unit) = apply {
            this.onDismissListener = listener
        }

        fun show(fragmentManager: FragmentManager, tag: String?): NoTitleDialog {
            val dialog = NoTitleDialog(
                body,
                positiveButtonText,
                negativeButtonText,
                positiveButtonListener,
                negativeButtonListener,
                onDismissListener
            )
            dialog.show(fragmentManager, tag)
            return dialog
        }
    }
}