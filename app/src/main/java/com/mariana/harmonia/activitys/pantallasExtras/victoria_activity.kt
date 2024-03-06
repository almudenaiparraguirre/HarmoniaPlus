package com.mariana.harmonia.activitys.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.mariana.harmonia.activitys.JuegoMusicalActivity
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.utils.Utils
import kotlinx.coroutines.runBlocking

class victoria_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private var precision: Int = 0
    private lateinit var victoriaTextView: TextView
    private lateinit var emogiTextView: TextView
    private lateinit var frasesTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)
        nivel = intent.getIntExtra("numeroNivel", 1)
        precision = intent.getIntExtra("precision", 0)
        actualizarDatos()

        val emojis = arrayOf("😄", "😃", "😁", "😊", "😆")
        val frases = arrayOf(
            "¡Excelente trabajo! 💪",
            "¡Eres un maestro! 🌟",
            "¡Lo lograste! 😎",
            "¡Increíble! 🚀",
            "¡Eres un campeón! 🏆",
            "¡Fantástico! 🎉",
            "¡Asombroso desempeño!",
            "¡Impecable! 👌",
            "¡Eres un experto! 💡",
            "¡Muy bien hecho! 👍",
            "¡Eres un genio! 🧠",
            "¡Maravilloso! ✨",
            "¡Eres un héroe! 🦸‍♂️",
            "¡Bravo! 👏",
            "¡Eres un ganador! 🥇",
            "¡Inmejorable! 💯",
            "¡Espectacular! 🌟",
            "¡Increíblemente bien! 🌈",
            "¡Estupendo! 🎈",
            "¡Fantástico desempeño! 💥",
            "¡Eres una leyenda! 🏅",
            "¡Eres impresionante! 🤩",
            "¡Sobresaliente! 🌠",
            "¡Eres un prodigio! 🌟",
            "¡Excepcional! 👌",
            "¡Espléndido! ✨",
            "¡Eres un fenómeno!",
            "¡Sobresaliente! 🏆",
            "¡Eres un talento natural!",
            "¡Impecable ejecución! 💪",
            "¡Eres un virtuoso! 🎻",
            "¡Magnífico! 🌟",
            "¡Impresionante! 🌟",
            "¡Eres un prodigio! 🌟",
            "¡Espectacular desempeño!",
            "¡Eres una inspiración!",
            "¡Estelar! 🌟",
            "¡Eres un fenómeno! 💥",
            "¡Brillante! ✨",
            "¡Eres imparable! 🌟",
            "¡Espectacular! 💥",
            "¡Eres un virtuoso! 🎹",
            "¡Asombroso! 🌟",
            "¡Eres una maravilla! 🌟",
            "¡Espléndido desempeño! 💥",
            "¡Increíble logro! 🏆",
            "¡Eres un prodigio! 🌟",
            "¡Espectacular ! 💥",
            "¡Eres una estrella! 🌟",
            "facilito 😎",
            "¡Espléndido! 🎉"
        )


        emogiTextView = findViewById(R.id.emogiTextView)
        victoriaTextView = findViewById(R.id.victoriaTextView)
        frasesTextView = findViewById(R.id.fraseTextView)
        Utilidades.degradadoTexto(this, victoriaTextView.id, R.color.rosa, R.color.morado)
        emogiTextView.text = emojis.random().toString()
        frasesTextView.text = frases.random().toString()
        mediaPlayer = MediaPlayer.create(this, R.raw.win_sound)
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.start()

    }

    private fun actualizarDatos() = runBlocking {
        try {
            Utils.setNivelActual(nivel + 1)
            Utils.setExperiencia((Utils.getExperiencia() ?: 0) + (20 + (nivel * 10 + 10)))
            var precisionesList = Utils.getPrecisiones()?.toMutableList() ?: MutableList(100) { 0 }
            precisionesList[nivel - 1] = precision
            Utils.setPrecisiones(precisionesList)
        } catch (e: IndexOutOfBoundsException) {
            // Manejar la excepción aquí
            println("Índice fuera de los límites: ${e.message}")
        }
    }


    fun irMenu(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }

    fun irRepetir(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)

    }

    fun irSiguiente(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", (nivel + 1))
        finish()
        startActivity(intent)
    }
}