package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class escribirNuevaContrasenaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escribir_nueva_contrasena)
    }

    fun confirmarContrasenaNueva(view: View){
        val intent = Intent(this, ContrasenaRestablecidaCorrectamenteActivity::class.java)
        startActivity(intent)
    }
}