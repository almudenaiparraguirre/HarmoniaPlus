package com.mariana.harmonia.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.PlantillaActivity

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_modo_juego)
        //colorearTexto(this, R.id.titleTextView)
    }

    fun menu_perfil(view: View){
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View){
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun clickOpciones(view: View){
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

}