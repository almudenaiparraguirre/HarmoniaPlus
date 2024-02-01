package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }

}