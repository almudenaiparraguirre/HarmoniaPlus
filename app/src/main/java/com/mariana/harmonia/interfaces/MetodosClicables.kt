package com.mariana.harmonia.interfaces

import android.app.Activity
import android.content.Intent
import android.view.View
import com.mariana.harmonia.InicioSesionActivity

abstract class MetodosClicables {
    fun irSalir(activity: Activity) {
        activity.finishAffinity()
    }

    fun irIniciarSesion(activity: Activity, view: View) {
        val intent = Intent(activity, InicioSesionActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}