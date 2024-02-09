package com.mariana.harmonia

import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import kotlin.random.Random

class NivelesAventuraActivity : AppCompatActivity() {

    private val numBotones = 50
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private lateinit var menuSuperior: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveles_aventura)

        llBotonera = findViewById(R.id.llBotonera)
        botonCorrecto = Random.nextInt(numBotones)
        menuSuperior = findViewById(R.id.llTopBar)

        val lp = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.button_width),
            resources.getDimensionPixelSize(R.dimen.button_height)
        )

        // Margen entre botones y margen superior
        lp.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin_top),
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin)
        )

        for (i in 0 until numBotones) {
            val button = Button(this)
            button.text = String.format("%02d", i)

            // Crear un nuevo conjunto de parámetros de diseño para cada botón
            val lp = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.button_width),
                resources.getDimensionPixelSize(R.dimen.button_height)
            )

            // Configurar el margen aleatorio
            val randomMargin =
                Random.nextInt(0, resources.getDimensionPixelSize(R.dimen.max_margin))
            lp.setMargins(
                if (i % 2 == 0) randomMargin else 0,
                resources.getDimensionPixelSize(R.dimen.button_margin_top),
                if (i % 2 != 0) randomMargin else 0,
                lp.bottomMargin
            )

            // Configurar la gravedad
            lp.gravity = Gravity.CENTER

            button.layoutParams = lp

            button.setOnClickListener(buttonClickListener(i))

            button.setBackgroundResource(getRandomButtonDrawable())
            llBotonera.addView(button)
        }
    }

    private fun buttonClickListener(index: Int): View.OnClickListener? {
        return View.OnClickListener {
            if (index == botonCorrecto) {
                Toast.makeText(
                    this@NivelesAventuraActivity,
                    "¡ME ENCONTRASTE!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@NivelesAventuraActivity,
                    "Sigue buscando",
                    Toast.LENGTH_SHORT
                ).show()
            }
            botonCorrecto = Random.nextInt(numBotones)
        }
    }


    private fun getRandomButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }

    /*private fun setRandomHorizontalPosition(button: Button, index: Int) {
        val layoutParams = button.layoutParams as LinearLayout.LayoutParams

        // Obtener el tamaño de la pantalla
        val screenWidth = resources.displayMetrics.widthPixels

        // Calcular la posición aleatoria
        val margin = resources.getDimensionPixelSize(R.dimen.button_margin)
        val buttonWidth = layoutParams.width
        val leftMargin: Int

        if (index % 2 == 0) {
            // Si es par, colocar a la derecha
            leftMargin = Random.nextInt(screenWidth - buttonWidth - margin, screenWidth - buttonWidth)
        } else {
            // Si es impar, colocar a la izquierda
            leftMargin = Random.nextInt(margin, screenWidth / 2)
        }

        // Configurar márgenes
        layoutParams.leftMargin = leftMargin

        // Aplicar cambios
        button.layoutParams = layoutParams
    }*/


    /*private fun setRandomVerticalPosition(button: Button) {
        val layoutParams = button.layoutParams as LinearLayout.LayoutParams

        // Obtener el número de secciones
        val numSecciones = 4 // Ajusta esto según la cantidad de secciones que desees

        // Calcular la sección aleatoria
        val randomSection = Random.nextInt(numSecciones)

        // Obtener la guía correspondiente a la sección
        val guidelineId = when (randomSection) {
            0 -> R.id.guideline1
            1 -> R.id.guideline2
            2 -> R.id.guideline3
            else -> R.id.llBotonera4 // o app:layout_constraintTop_toBottomOf="@id/guideline3"
        }

        // Obtener la posición vertical de la guía
        val guidelinePosition = findViewById<Guideline>(guidelineId).layoutParams.height

        // Calcular la posición del botón
        val buttonHeight = button.height
        val randomPosition = Random.nextFloat() * guidelinePosition

        // Configurar márgenes superiores e inferiores del botón
        layoutParams.topMargin = (randomPosition * guidelinePosition * buttonHeight).toInt()
        layoutParams.bottomMargin = (guidelinePosition - randomPosition) * guidelinePosition * buttonHeight).toInt() - buttonHeight

        button.layoutParams = layoutParams
    }*/
}