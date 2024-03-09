package com.mariana.harmonia.activitys.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mariana.harmonia.R

class CargaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño del fragmento que contiene los botones
        return inflater.inflate(R.layout.fragment_carga, container, false)
    }

}


