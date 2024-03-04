package com.mariana.harmonia.activitys.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.utils.Utils
import kotlinx.coroutines.runBlocking

class victoria_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nivel = intent.getIntExtra("numeroNivel", 1)
        actualizarDatos()

        setContentView(R.layout.activity_victoria)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
    }

    private fun actualizarDatos()= runBlocking {
        Utils.setNivelActual((nivel+1))
        Utils.setExperiencia(Utils.getExperiencia()!!.toInt()+(20+(nivel*10+10)))
    }


    fun irMenu(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }

    fun irRepetir(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)

    }
    fun irSiguiente(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", (nivel+1))
        finish()
        startActivity(intent)
    }
}