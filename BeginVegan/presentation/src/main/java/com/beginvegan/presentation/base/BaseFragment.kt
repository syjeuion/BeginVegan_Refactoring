package com.beginvegan.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.beginvegan.presentation.util.OnThrottleClickListener
import timber.log.Timber

typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding ?: error("Base Fragment binding Error")
    private val tag = "${this::class.java.simpleName}"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        logMessage("onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logMessage("onViewCreated")
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logMessage("onDestroyView")
        _binding = null
    }

    protected abstract fun init()
    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun logMessage(message: String) {
        Timber.tag(tag).d(message)
    }

    protected fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }
}