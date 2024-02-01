package com.mariana.harmonia.interfaces

import android.app.Activity
import android.content.Intent
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.activitys.Utilidades

interface PlantillaActivity {
     fun Salir(activity: Activity) {

         Utilidades.salirAplicacion(activity)
    }

    fun IniciarSesion(activity: Activity) {
        val intent = Intent(activity, InicioSesionActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}