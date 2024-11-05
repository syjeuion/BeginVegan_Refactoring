package com.beginvegan.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.beginvegan.presentation.util.OnThrottleClickListener
import timber.log.Timber

abstract class BaseActivity<VB : ViewBinding>() : AppCompatActivity() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private val tag = "${this::class.java.simpleName}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logMessage("onCreate")
        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)

        init()
        initViewModel()
    }
    abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

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