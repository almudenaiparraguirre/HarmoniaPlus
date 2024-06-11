package com.mariana.harmonia.activities.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.mariana.harmonia.activities.JuegoMusicalActivity
import com.mariana.harmonia.activities.NivelesAventuraActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking

/**
 * Actividad que se muestra al jugador al perder en un nivel del juego.
 *
 * @property nivel Número del nivel en el que se perdió.
 * @property mediaPlayer Reproductor de audio para los sonidos de la actividad.
 * @property derrotaTextView TextView que muestra el mensaje de derrota.
 * @property emojiTextView TextView que muestra un emoji aleatorio.
 * @property frasesTextView TextView que muestra un mensaje de ánimo aleatorio.
 */
class victoria_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private var precision: Int = 0
    private lateinit var victoriaTextView: TextView
    private lateinit var emogiTextView: TextView
    private lateinit var frasesTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.victoria_activity)
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
        Utils.degradadoTexto(this, victoriaTextView.id, R.color.rosa, R.color.morado)
        emogiTextView.text = emojis.random().toString()
        frasesTextView.text = frases.random().toString()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_pantallas_fin)
        imageView.startAnimation(anim)

        mediaPlayer = MediaPlayer.create(this, R.raw.win_sound)
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.start()
    }

    /**
     * Actualiza los datos relacionados con el progreso del jugador, como el nivel actual,
     * la experiencia acumulada y las precisiones por nivel.
     *
     * Se incrementa el nivel actual en 1, se suma la experiencia obtenida al total acumulado,
     * y se actualiza la lista de precisiones con la precisión del nivel actual.
     *
     * @throws IndexOutOfBoundsException Manejada internamente en caso de que el índice del nivel esté fuera de los límites.
     */
    private fun actualizarDatos() = runBlocking {
        try {

            var nivelMaximo = UtilsDB.getNivelMaximo()
            if(nivelMaximo!! == nivel) {
                UtilsDB.setExperiencia((UtilsDB.getExperiencia() ?: 0) + (20 + (nivel * 10 + 10)))
                UtilsDB.setNivelMaximo(UtilsDB.getNivelMaximo()!! + 1)
            }

            var precisionesList = UtilsDB.getPrecisiones()?.toMutableList() ?: MutableList(100) { 0 }
            precisionesList[nivel - 1] = precision
            UtilsDB.setPrecisiones(precisionesList)
        } catch (e: IndexOutOfBoundsException) {
            // Manejar la excepción aquí
            println("Índice fuera de los límites: ${e.message}")
        }
    }

    /**
     * Método invocado al presionar el botón para regresar al menú principal.
     * Reproduce un sonido y redirige al menú de niveles.
     *
     * @param view Vista del botón que activa la función.
     */
    fun irMenu(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        finish()
        startActivity(intent)
    }

    /**
     * Método invocado al presionar el botón para repetir el nivel.
     * Reproduce un sonido y redirige a la pantalla del juego con el mismo nivel.
     *
     * @param view Vista del botón que activa la función.
     */
    fun irRepetir(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)

    }

    /**
     * Método invocado al presionar el botón para pasar al
     * siguiente nivel. Reproduce un sonido y redirige al siguiente
     * nivel
     *
     * @param view Vista del botón que activa la función.
     */
    fun irSiguiente(view: View) {
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", (nivel + 1))
        finish()
        startActivity(intent)
    }
}