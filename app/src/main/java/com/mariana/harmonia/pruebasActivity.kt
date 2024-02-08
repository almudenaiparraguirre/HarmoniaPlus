package com.mariana.harmonia

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView

class pruebasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas)


        findViewById<ImageView>(R.id.notaDo_).setOnClickListener { clickDo() }
        findViewById<ImageView>(R.id.notaRe_).setOnClickListener { clickRe() }
        findViewById<ImageView>(R.id.notaFa_).setOnClickListener { clickFa() }
        findViewById<ImageView>(R.id.notaSol_).setOnClickListener { clickSol() }
        findViewById<ImageView>(R.id.notaLa_).setOnClickListener { clickLa() }

        findViewById<ImageView>(R.id.notaDo).setOnClickListener { clickDo() }
        findViewById<ImageView>(R.id.notaRe).setOnClickListener { clickRe() }
        findViewById<ImageView>(R.id.notaMi).setOnClickListener { clickMi() }
        findViewById<ImageView>(R.id.notaFa).setOnClickListener { clickFa() }
        findViewById<ImageView>(R.id.notaSol).setOnClickListener { clickSol() }
        findViewById<ImageView>(R.id.notaLa).setOnClickListener { clickLa() }
        findViewById<ImageView>(R.id.notaSi).setOnClickListener { clickSi() }

    }

    private fun playSound(soundFile: String) {
        val mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(soundFile, "raw", packageName))
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }
    private fun clickDo() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Do")
        playSound("note_do")
    }

    private fun clickRe() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Re")
        playSound("note_re")
    }

    private fun clickMi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Mi")
        playSound("note_mi")
    }

    private fun clickFa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Fa")
        playSound("note_fa")
    }

    private fun clickSol() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Sol")
        playSound("note_sol")
    }

    private fun clickLa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota La")
        playSound("note_la")
    }

    private fun clickSi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Si")
        playSound("note_si")
    }

        // Métodos correspondientes a los onClick definidos en el XML para las notas negras

        fun clickDo_(view: View) {
            Log.d("pruebasActivity", "Se ha hecho clic en el método clickDo_")
            // Lógica para cuando se haga clic en la nota Do_
        }

        fun clickRe_(view: View) {
            Log.d("pruebasActivity", "Se ha hecho clic en el método clickRe_")
            // Lógica para cuando se haga clic en la nota Re_
        }

        fun clickFa_(view: View) {
            Log.d("pruebasActivity", "Se ha hecho clic en el método clickFa_")
            // Lógica para cuando se haga clic en la nota Fa_
        }

        fun clickSol_(view: View) {
            Log.d("pruebasActivity", "Se ha hecho clic en el método clickSol_")
            // Lógica para cuando se haga clic en la nota Sol_
        }

        fun clickLa_(view: View) {
            Log.d("pruebasActivity", "Se ha hecho clic en el método clickLa_")
            // Lógica para cuando se haga clic en la nota La_
        }

}