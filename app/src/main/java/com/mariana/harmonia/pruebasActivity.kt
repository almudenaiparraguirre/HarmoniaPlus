package com.mariana.harmonia

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

class pruebasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas)


        findViewById<ImageView>(R.id.notaRe_b).setOnClickListener { clickRe_b() }
        findViewById<ImageView>(R.id.notaMi_b).setOnClickListener { clickMi_b() }
        findViewById<ImageView>(R.id.notaSol_b).setOnClickListener { clickSol_b() }
        findViewById<ImageView>(R.id.notaLa_b).setOnClickListener { clickLa_b() }
        findViewById<ImageView>(R.id.notaSi_b).setOnClickListener { clickSi_b() }

        findViewById<ImageView>(R.id.notaDo).setOnClickListener { clickDo() }
        findViewById<ImageView>(R.id.notaRe).setOnClickListener { clickRe() }
        findViewById<ImageView>(R.id.notaMi).setOnClickListener { clickMi() }
        findViewById<ImageView>(R.id.notaFa).setOnClickListener { clickFa() }
        findViewById<ImageView>(R.id.notaSol).setOnClickListener { clickSol() }
        findViewById<ImageView>(R.id.notaLa).setOnClickListener { clickLa() }
        findViewById<ImageView>(R.id.notaSi).setOnClickListener { clickSi() }

    }

    private fun playSound(soundFile: String) {
        val mediaPlayer =
            MediaPlayer.create(this, resources.getIdentifier(soundFile, "raw", packageName))
        val volume = 1f
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }

    private fun clickDo() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Do")
        playSound("c4")
    }

    fun clickRe_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Reb")
        playSound("db4")
    }

    private fun clickRe() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Re")
        playSound("d4")
    }


    fun clickMi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Mib")
        playSound("eb4")
    }


    private fun clickMi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Mi")
        playSound("e4")
    }

    private fun clickFa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Fa")
        playSound("f4")
    }

    fun clickSol_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Solb")
        playSound("gb4")
    }

    private fun clickSol() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Sol")
        playSound("g4")
    }


    fun clickLa_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Lab")
        playSound("ab4")
    }

    private fun clickLa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota La")
        playSound("a4")
    }

    fun clickSi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Sib")
        playSound("bb4")
    }

    private fun clickSi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Si")
        playSound("b4")
    }

    // Métodos correspondientes a los onClick definidos en el XML para las notas negras


}