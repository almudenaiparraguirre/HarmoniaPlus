package com.mariana.harmonia.activitys.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import com.mariana.harmonia.R

class derrota_activity : AppCompatActivity() {

    private var nivel: Int = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        nivel = intent.getIntExtra("numeroNivel", 1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_derrota)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

    }

    fun irRepetir(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)
    }
    fun irMenu(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }
}