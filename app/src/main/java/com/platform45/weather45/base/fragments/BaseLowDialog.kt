package com.platform45.weather45.base.fragments

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseLowDialog : BaseDialogFragment(), OnFragmentBackPressed {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.setCancelable(false)
        isCancelable = false
        dialog?.setCanceledOnTouchOutside(false)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.window?.setDimAmount(0.4f)
        var parentView = super.onCreateView(inflater, container, savedInstanceState)

        parentView?.isFocusableInTouchMode = true
        parentView?.requestFocus()
        parentView?.setOnKeyListener { v, keyCode, event ->

            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackBtnPressed()
                true
            }

            false
        }

        return parentView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
        }
    }

    companion object {
        fun newInstance(context: Activity): Fragment {
            return Fragment.instantiate(context, BaseLowDialog::class.java.name, null)
        }
    }
}

interface OnFragmentBackPressed {
    fun onBackBtnPressed()
}