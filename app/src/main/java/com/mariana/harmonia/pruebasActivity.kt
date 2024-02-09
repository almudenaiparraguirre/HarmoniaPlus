package com.mariana.harmonia

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


class pruebasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas)

        // Click notas negras
        val notaRe_b = findViewById<ImageView>(R.id.notaRe_b)
        val notaMi_b = findViewById<ImageView>(R.id.notaMi_b)
        val notaSol_b = findViewById<ImageView>(R.id.notaSol_b)
        val notaLa_b = findViewById<ImageView>(R.id.notaLa_b)
        val notaSi_b = findViewById<ImageView>(R.id.notaSi_b)

        // Click notas blancas
        val notaDo = findViewById<ImageView>(R.id.notaDo)
        val notaRe = findViewById<ImageView>(R.id.notaRe)
        val notaMi = findViewById<ImageView>(R.id.notaMi)
        val notaFa = findViewById<ImageView>(R.id.notaFa)
        val notaSol = findViewById<ImageView>(R.id.notaSol)
        val notaLa = findViewById<ImageView>(R.id.notaLa)
        val notaSi = findViewById<ImageView>(R.id.notaSi)

        //Activamos los listeners

        notaRe_b.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickRe_b()
                }

                MotionEvent.ACTION_UP -> {
                    soltarRe_b()
                }
            }
            true
        }

        notaMi_b.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickMi_b()
                }

                MotionEvent.ACTION_UP -> {
                    soltarMi_b()
                }
            }
            true
        }

        notaSol_b.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickSol_b()
                }

                MotionEvent.ACTION_UP -> {
                    soltarSol_b()
                }
            }
            true
        }

        notaLa_b.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickLa_b()
                }

                MotionEvent.ACTION_UP -> {
                    soltarLa_b()
                }
            }
            true
        }

        notaSi_b.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickSi_b()
                }

                MotionEvent.ACTION_UP -> {
                    soltarSi_b()
                }
            }
            true
        }



        notaDo.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickDo()
                }

                MotionEvent.ACTION_UP -> {
                    soltarDo()
                }
            }
            true
        }

        notaRe.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickRe()
                }

                MotionEvent.ACTION_UP -> {
                    soltarRe()
                }
            }
            true
        }

        notaMi.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickMi()
                }

                MotionEvent.ACTION_UP -> {
                    soltarMi()
                }
            }
            true
        }

        notaFa.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickFa()
                }

                MotionEvent.ACTION_UP -> {
                    soltarFa()
                }
            }
            true
        }

        notaSol.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickSol()
                }

                MotionEvent.ACTION_UP -> {
                    soltarSol()
                }
            }
            true
        }

        notaLa.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickLa()
                }

                MotionEvent.ACTION_UP -> {
                    soltarLa()
                }
            }
            true
        }

        notaSi.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    clickSi()
                }

                MotionEvent.ACTION_UP -> {
                    soltarSi()
                }
            }
            true
        }
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
        actualizarFondoBlancas(R.id.notaDo, R.drawable.svg_tecla_do_clicada, this)
    }

    fun clickRe_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Reb")
        playSound("db4")
        actualizarFondoNegras(R.id.fondoReB, R.drawable.style_buttond_egradado_suave)
    }

    private fun clickRe() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Re")
        playSound("d4")
        actualizarFondoBlancas(R.id.notaRe, R.drawable.svg_tecla_re_clicada, this)
    }


    fun clickMi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Mib")
        playSound("eb4")
        actualizarFondoNegras(R.id.fondoMiB, R.drawable.style_buttond_egradado_suave)
    }


    private fun clickMi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Mi")
        playSound("e4")
        actualizarFondoBlancas(R.id.notaMi, R.drawable.svg_tecla_mi_clicada, this)
    }

    private fun clickFa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Fa")
        playSound("f4")
        actualizarFondoBlancas(R.id.notaFa, R.drawable.svg_tecla_fa_clicada, this)
    }

    fun clickSol_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Solb")
        playSound("gb4")
        actualizarFondoNegras(R.id.fondoSolB, R.drawable.style_buttond_egradado_suave)
    }

    private fun clickSol() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Sol")
        playSound("g4")
        actualizarFondoBlancas(R.id.notaSol, R.drawable.svg_tecla_sol_clicada, this)
    }


    fun clickLa_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Lab")
        playSound("ab4")
        actualizarFondoNegras(R.id.fondoLaB, R.drawable.style_buttond_egradado_suave)

    }

    private fun clickLa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota La")
        playSound("a4")
        actualizarFondoBlancas(R.id.notaLa, R.drawable.svg_tecla_la_clicada, this)
    }

    fun clickSi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Sib")
        playSound("bb4")
        actualizarFondoNegras(R.id.fondoSiB, R.drawable.style_buttond_egradado_suave)
    }

    private fun clickSi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Si")
        playSound("b4")
        actualizarFondoBlancas(R.id.notaSi, R.drawable.svg_tecla_si_clicada, this)
    }


        //Soltar

    private fun soltarDo() {
        Log.d("pruebasActivity", "Se ha soltado la nota Do")
    }

    private fun soltarRe_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota reB")
        actualizarFondoNegras(R.id.fondoReB, R.drawable.style_buttond_egradado_suave)
    }

    private fun soltarRe() {
        Log.d("pruebasActivity", "Se ha soltado la nota Re")
    }

    private fun soltarMi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mib")
    }

    private fun soltarMi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mi")
    }

    private fun soltarFa() {
        Log.d("pruebasActivity", "Se ha soltado la nota Fa")
    }

    private fun soltarSol_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Solb")
    }

    private fun soltarSol() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sol")
    }

    private fun soltarLa_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Lab")
    }

    private fun soltarLa() {
        Log.d("pruebasActivity", "Se ha soltado la nota La")
    }

    private fun soltarSi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sib")
    }

    private fun soltarSi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Si")
    }

}