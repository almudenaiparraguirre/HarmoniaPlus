package com.mariana.harmonia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity

class victoria_activity : AppCompatActivity() {
    private var nivel: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nivel = intent.getIntExtra("numeroNivel", 1)
        setContentView(R.layout.activity_victoria)
    }

    fun irMenu(view: View) {
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }

    fun irRepetir(view: View) {
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)

    }
    fun irSiguiente(view: View) {
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", (nivel+1))
        finish()
        startActivity(intent)
    }
}