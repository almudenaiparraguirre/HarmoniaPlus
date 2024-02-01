package com.mariana.harmonia

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mariana.harmonia.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.MetodosClicables
import com.mariana.harmonia.interfaces.PlantillaActivity
import kotlin.system.exitProcess

class InicioSesionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        colorearTexto(this, R.id.titleTextView)
        colorearTexto(this, R.id.registrateTextView)
        colorearTexto(this, R.id.recuerdasContrasena)
    }


    fun clickCrearCuenta(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun clickNoRecuerdasLaContraseña(view: View){
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
    }

    fun irRegistrate(view: View?){
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }
}
