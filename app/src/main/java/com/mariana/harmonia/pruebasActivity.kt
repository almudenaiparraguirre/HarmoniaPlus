package com.mariana.harmonia

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


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

    private fun actualizarFondoNegras(viewId: Int, drawableId: Int) {
        val view = findViewById<View>(viewId)
        val drawable: Drawable? = ContextCompat.getDrawable(this, drawableId)
        drawable?.let {
            view.background = it
        }
    }


    fun actualizarFondoBlancas(imageViewId: Int, nuevaImagenId: Int, actividad: AppCompatActivity) {
        // Buscar el ImageView por su ID
        val imageView = actividad.findViewById<ImageView>(imageViewId)
        // Establecer la nueva imagen
        imageView.setImageResource(nuevaImagenId)
    }


    private fun clickDo() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Do")
        playSound("c4")
        actualizarFondoBlancas(R.id.notaDo,R.drawable.svg_tecla_do_clicada,this)
    }

    fun clickRe_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Reb")
        playSound("db4")
        actualizarFondoNegras(R.id.fondoReB,R.drawable.style_buttond_egradado_suave)
    }

    private fun clickRe() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Re")
        playSound("d4")
        actualizarFondoBlancas(R.id.notaRe,R.drawable.svg_tecla_re_clicada,this)
    }


    fun clickMi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Mib")
        playSound("eb4")
        actualizarFondoNegras(R.id.fondoMiB,R.drawable.style_buttond_egradado_suave)
    }


    private fun clickMi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Mi")
        playSound("e4")
        actualizarFondoBlancas(R.id.notaMi,R.drawable.svg_tecla_mi_clicada,this)
    }

    private fun clickFa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Fa")
        playSound("f4")
        actualizarFondoBlancas(R.id.notaFa,R.drawable.svg_tecla_fa_clicada,this)
    }

    fun clickSol_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Solb")
        playSound("gb4")
        actualizarFondoNegras(R.id.fondoSolB,R.drawable.style_buttond_egradado_suave)
    }

    private fun clickSol() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Sol")
        playSound("g4")
        actualizarFondoBlancas(R.id.notaSol,R.drawable.svg_tecla_sol_clicada,this)
    }


    fun clickLa_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Lab")
        playSound("ab4")
        actualizarFondoNegras(R.id.fondoLaB,R.drawable.style_buttond_egradado_suave)

    }

    private fun clickLa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota La")
        playSound("a4")
        actualizarFondoBlancas(R.id.notaLa,R.drawable.svg_tecla_la_clicada,this)
    }

    fun clickSi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Sib")
        playSound("bb4")
        actualizarFondoNegras(R.id.fondoSiB,R.drawable.style_buttond_egradado_suave)
    }

    private fun clickSi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Si")
        playSound("b4")
        actualizarFondoBlancas(R.id.notaSi,R.drawable.svg_tecla_si_clicada,this)
    }

    // Métodos correspondientes a los onClick definidos en el XML para las notas negras


}