package com.mariana.harmonia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mariana.harmonia.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.PlantillaActivity

class InicioSesionActivity : AppCompatActivity(),PlantillaActivity {

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
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }
}
