package com.mariana.harmonia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class InicioSesionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prueba_inicio_sesion_ajustar)
    }

    fun tuMetodoBoton(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun tuMetodoSalir(view: View?) {
        finish()
    }
}