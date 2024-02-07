package com.mariana.harmonia.interfaces

import android.app.Activity
import android.content.Intent
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.activitys.Utilidades

interface PlantillaActivity {
     fun Salir(activity: Activity) {

         Utilidades.salirAplicacion(activity)
    }

    fun IniciarSesion(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}