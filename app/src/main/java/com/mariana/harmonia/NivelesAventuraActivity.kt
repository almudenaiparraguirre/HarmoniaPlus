package com.mariana.harmonia

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random

class NivelesAventuraActivity : AppCompatActivity() {

    private val numBotones = 50
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private var idNivelNoCompletado: Int = 0
    private lateinit var menuSuperior: LinearLayout
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveles_aventura)
        idNivelNoCompletado = obtenerIdPrimerNivelNoCompletado()!!
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

            val button: View = if (i > idNivelNoCompletado) {
                createLockedButton()
            } else {
                createUnlockedButton(i)
            }

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

            if(i > idNivelNoCompletado){
                button.isEnabled = false
                button.setBackgroundResource(getRandomUnlockedButtonDrawable())
            }
            else {

                button.setBackgroundResource(getRandomButtonDrawable())
                button.setOnClickListener {
                    val numeroNivel = button.id
                    val intent = Intent(this, JuegoMusicalActivity::class.java)
                    intent.putExtra("numeroNivel", numeroNivel)
                    startActivity(intent)
                }
            }

            // Configurar la gravedad
            lp.gravity = Gravity.CENTER

            button.layoutParams = lp

            //button.setOnClickListener(buttonClickListener(i))
            llBotonera.addView(button)
        }
    }

    private fun createLockedButton(): ImageButton {
        val lockedButton = ImageButton(this)
        lockedButton.setBackgroundResource(R.drawable.style_round_button_blue) // Establece el fondo según tus necesidades
        lockedButton.setImageResource(R.drawable.lock) // Establece el icono de candado
        lockedButton.isEnabled = false // Deshabilita el botón bloqueado
        return lockedButton
    }

    private fun createUnlockedButton(levelNumber: Int): Button {
        val button = Button(this)
        button.id = levelNumber
        button.text = String.format("%2d", levelNumber)
        button.setBackgroundResource(getRandomButtonDrawable())
        button.setOnClickListener {
            val numeroNivel = button.id
            val intent = Intent(this, JuegoMusicalActivity::class.java)
            intent.putExtra("numeroNivel", numeroNivel)
            startActivity(intent)
        }
        return button
    }

    private fun getRandomButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }
    private fun getRandomUnlockedButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button_blue
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }

    private fun obtenerIdPrimerNivelNoCompletado(): Int? {
        val nivelesJson = obtenerNivelesJSON()
        val nivelesArray = nivelesJson?.getJSONArray("niveles")

        if (nivelesArray != null) {
            for (i in 0 until nivelesArray.length()) {
                val nivel = nivelesArray.getJSONObject(i)
                val completado = nivel.getBoolean("completado")

                if (!completado) {
                    return nivel.getInt("id")
                }
            }
        }
        return null
    }
    private fun obtenerNivelesJSON(): JSONObject? {
        var nivelesJson: JSONObject? = null
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.info_niveles)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            nivelesJson = JSONObject(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return nivelesJson
    }
}