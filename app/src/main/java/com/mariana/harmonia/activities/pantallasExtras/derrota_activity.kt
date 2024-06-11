package com.mariana.harmonia.activities.pantallasExtras

import android.app.AlertDialog
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
 * @property emogiTextView TextView que muestra un emoji aleatorio.
 * @property frasesTextView TextView que muestra un mensaje de ánimo aleatorio.
 */
class derrota_activity : AppCompatActivity() {

    private var nivel: Int = 0
    private var vidas: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var derrotaTextView: TextView
    private lateinit var emogiTextView: TextView
    private lateinit var frasesTextView: TextView


    /**
     * Método llamado al crear la actividad. Se encarga de inicializar la interfaz de usuario
     * y reproducir sonidos de derrota.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        nivel = intent.getIntExtra("numeroNivel", 1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.derrota_activity)
        val emojis = arrayOf("😞", "😔", "😕", "🙁", "☹️", "😟", "😢", "😭")
        val frases = arrayOf(
            "¡Sigue intentando!😊",
            "¡Otra vez será...💪",
            "¡Casi lo logras!😅",
            "¡Sigue practicando!🎶",
            "¡A seguir intentando! ",
            "¡Cerca de ganar!",
            "¡Inténtalo otra vez!",
            "¡Persiste y vencerás!🏆",
            "¡Por poco lo consigues!😮",
            "¡No te rindas!🚀",
            "¡Sigue adelante! 🚶‍♂️",
            "¡No te desanimes...😔",
            "¡Sigue practicando más!🔥",
            "¡Mejor suerte luego! 🌟",
            "¡Casi, casi lo tienes!🤞",
            "¡No muy lejos...",
            "¡Ánimo, casi esta!💖",
            "¡Buen esfuerzo!👏",
            "¡A seguir intentándolo!",
            "¡Casi lo alcanzas!",
            "¡Prueba otra vez...!",
            "¡Sigue adelante!",
            "¡Persiste en ello!",
            "¡Casi alcanzas la meta!",
            "¡Casi lo atrapas",
            "¡Casi lo logras... 😓",
            "¡Continúa intentando...",
            "¡Por poco lo logras...😥",
            "¡Sigue practicando duro! 💪🎵",
            "¡Sigue intentándolo",
            "¡Más suerte la próxima...🍀",
            "¡Continúa esforzándote💪",
            "¡Por poco no lo logras...😟",
            "¡Sigue practicando más!",
            "¡No muy lejos ahora... 🌟",
            "¡Casi lo dominas!🎹",
            "¡Sigue esforzándote más💪🔥",
            "¡Mejor suerte la próxima...🍀",
            "¡Por poco no lo tienes...😕",
            "¡No muy lejos!🚶‍♂️",
            "¡Sigue intentándolo!",
            "¡A seguir practicando...",
            "¡Sigue luchando... ",
            "Un mono juega mejor... ",
            "¡Casi lo consigues! 😄",
            "¡Por poco lo alcanzas!😓",
            "¡Sigue intentándolo!",
            "¡No muy lejos!",
            "¡Sigue adelante! "
        )

        emogiTextView = findViewById(R.id.emogiTextView)
        derrotaTextView = findViewById(R.id.derrotaTextView)
        frasesTextView = findViewById(R.id.fraseTextView)
        Utils.degradadoTexto(this, derrotaTextView.id, R.color.rojo, R.color.negro)
        emogiTextView.text = emojis.random().toString()
        frasesTextView.text = frases.random().toString()
        mediaPlayer = MediaPlayer.create(this, R.raw.lose_sound)
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.start()
        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_pantallas_fin)
        imageView.startAnimation(anim)
        cargarBaseDatos()



    }

    private fun cargarBaseDatos()= runBlocking {
        vidas = UtilsDB.getVidas()!!.toInt()
        UtilsDB.setVidas(vidas -1)

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
        if(vidas <= 0){
            noVidas()
        }
        else{
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", nivel)
        finish()
        startActivity(intent)}
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

    private fun noVidas() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("No tienes vidas suficientes para jugar")
            .setPositiveButton("Aceptar") { dialog, _ ->
                // Aquí puedes realizar alguna acción adicional si es necesario
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}