package com.platform45.weather45.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.platform45.weather45.MyDrawerController

abstract class BaseFragment : Fragment() {
protected lateinit var myDrawerController: MyDrawerController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDrawerController = activity as MyDrawerController
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun initActivity(){

    }
}