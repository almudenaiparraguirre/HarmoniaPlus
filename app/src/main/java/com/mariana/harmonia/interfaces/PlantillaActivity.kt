package com.mariana.harmonia.interfaces

import android.app.Activity
import android.content.Intent
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.utils.Utils

interface PlantillaActivity {
     fun Salir(activity: Activity) {

         Utils.salirAplicacion(activity)
    }

    fun IniciarSesion(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}