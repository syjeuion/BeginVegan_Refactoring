package com.beginvegan.presentation.adapter.onboarding

import android.R
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class OnboardingAdapter(context: Context?, resource: Int, items: List<String?>?) :
    ArrayAdapter<String?>(context!!, resource, items!!) {
    private var selectedItemPosition: Int = -1

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<View>(R.id.text1) as TextView

        if (position == selectedItemPosition) {
            textView.setBackgroundColor(Color.parseColor("#FFE4F1EB"))
        } else {
            textView.setBackgroundColor(Color.TRANSPARENT)
        }

        return view
    }

}

