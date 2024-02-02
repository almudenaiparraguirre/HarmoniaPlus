package com.mariana.harmonia.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.InicioSesionActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.activitys.iniciarSesion.NivelesAventuraActivity
import com.mariana.harmonia.interfaces.PlantillaActivity

class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elige_modo_juego)
        //colorearTexto(this, R.id.titleTextView)
    }

    // FUN --> Ir al perfil del usuario
    fun menu_perfil(view: View){
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Vuelve a la pantalla de inicio de sesión
    fun cerrarSesion(view: View){
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
        finish()
    }

    // FUN --> Ir a la pantalla de opciones
    fun clickOpciones(view: View){
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    // FUN --> Ir a los niveles de aventura
    fun irModoAventura(){
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
    }
}