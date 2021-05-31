package com.platform45.weather45.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.platform45.weather45.MyDrawerController

abstract class BaseFragment : Fragment() {
protected lateinit var myDrawerController: MyDrawerController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDrawerController = activity as MyDrawerController
    }
}