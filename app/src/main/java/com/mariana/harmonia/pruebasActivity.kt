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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


class pruebasActivity : AppCompatActivity() {
    private lateinit var imagenNota: ImageView
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

        imagenNota = findViewById(R.id.imagenNota)

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


     private fun cambiarImagen(nombreArchivo: String) {
        // Obtén el ID de la imagen utilizando el nombre del archivo
        val idImagen = resources.getIdentifier(nombreArchivo, "drawable", packageName)

        // Verifica si el ID de la imagen es válido
        if (idImagen != 0) {
            // Cambia la imagen del ImageView
            imagenNota.setImageResource(idImagen)
        } else {
            // Si no se encuentra la imagen, puedes manejar el error aquí
            // Por ejemplo, podrías cargar una imagen de error predeterminada
            // o mostrar un mensaje de error al usuario
            Toast.makeText(this, "Imagen no encontrada", Toast.LENGTH_SHORT).show()
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
        cambiarImagen("nota_6d")
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
        actualizarFondoBlancas(R.id.notaDo, R.drawable.svg_tecla_do, this)
    }

    private fun soltarRe_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota reB")
        actualizarFondoNegras(R.id.fondoReB, R.drawable.sytle_degradado_fondo_piano)
    }

    private fun soltarRe() {
        Log.d("pruebasActivity", "Se ha soltado la nota Re")
        actualizarFondoBlancas(R.id.notaRe, R.drawable.svg_tecla_re, this)
    }

    private fun soltarMi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mib")
        actualizarFondoNegras(R.id.fondoMiB, R.drawable.sytle_degradado_fondo_piano)
    }

    private fun soltarMi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mi")
        actualizarFondoBlancas(R.id.notaMi, R.drawable.svg_tecla_mi, this)
    }

    private fun soltarFa() {
        Log.d("pruebasActivity", "Se ha soltado la nota Fa")
        actualizarFondoBlancas(R.id.notaFa, R.drawable.svg_tecla_fa, this)
    }

    private fun soltarSol_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Solb")
        actualizarFondoNegras(R.id.fondoSolB, R.drawable.sytle_degradado_fondo_piano)
    }

    private fun soltarSol() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sol")
        actualizarFondoBlancas(R.id.notaSol, R.drawable.svg_tecla_sol, this)
    }

    private fun soltarLa_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Lab")
        actualizarFondoNegras(R.id.fondoLaB, R.drawable.sytle_degradado_fondo_piano)
    }

    private fun soltarLa() {
        Log.d("pruebasActivity", "Se ha soltado la nota La")
        actualizarFondoBlancas(R.id.notaLa, R.drawable.svg_tecla_la, this)
    }

    private fun soltarSi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sib")
        actualizarFondoNegras(R.id.fondoSiB, R.drawable.sytle_degradado_fondo_piano)
    }

    private fun soltarSi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Si")
        actualizarFondoBlancas(R.id.notaSi, R.drawable.svg_tecla_si, this)
    }

}