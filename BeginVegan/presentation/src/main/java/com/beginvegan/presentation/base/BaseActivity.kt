package com.beginvegan.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.beginvegan.presentation.util.OnThrottleClickListener
import timber.log.Timber

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    AppCompatActivity() {
    lateinit var binding: T

    private val tag = "${this::class.java.simpleName}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logMessage("onCreate")
        binding = DataBindingUtil.setContentView(this, layoutResId)
        init()
        initViewModel()
    }

    protected abstract fun initViewModel()
    protected abstract fun init()
    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun logMessage(message: String) {
        Timber.tag(tag).d(message)
    }


    protected fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

}