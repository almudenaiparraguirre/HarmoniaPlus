package com.mariana.harmonia.activitys.iniciarSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.interfaces.PlantillaActivity

class RegistroActivity : AppCompatActivity(), PlantillaActivity {

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        Utilidades.colorearTexto(this, R.id.titleTextView)
    }

    // FUN --> Volver al inicio de sesión
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Salir de la aplicación
    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }
}