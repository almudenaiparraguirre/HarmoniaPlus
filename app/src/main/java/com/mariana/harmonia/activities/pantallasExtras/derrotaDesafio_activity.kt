package com.mariana.harmonia.activities.pantallasExtras

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.mariana.harmonia.activities.JuegoMusicalActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.utils.HashUtils
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking

/**
 * Actividad que se muestra al jugador al perder en un desafío del juego.
 *
 * @property nivel Número del nivel en el que se perdió el desafío.
 * @property notasHacertadas Número de notas musicales acertadas en el desafío.
 * @property tiempoDurado Tiempo total transcurrido en el desafío.
 * @property dificultad Nivel de dificultad del desafío.
 * @property textViewResultadoTotal TextView que muestra el resultado total de notas acertadas.
 * @property textViewResultadoTiempo TextView que muestra el resultado del tiempo transcurrido.
 * @property mediaPlayer Reproductor de audio para los sonidos de la actividad.
 * @property finTextView TextView que muestra el mensaje de fin del desafío.
 * @property emogiTextView TextView que muestra un emoji aleatorio.
 */
class derrotaDesafio_activity : AppCompatActivity() {
    private var nivel: Int = 0
    private var nombre: String ="desconocido"
    private var notasHacertadas: Int = 0
    private var tiempoDurado: Int = 0
    private var dificultad: Int = 0
    private lateinit var textViewResultadoTotal: TextView
    private lateinit var textViewResultadoTiempo: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var finTextView: TextView
    private lateinit var emogiTextView: TextView

    /**
     * Método llamado al crear la actividad. Se encarga de inicializar la interfaz de usuario,
     * procesar variables de la intención y reproducir sonidos específicos de la derrota en desafío.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.derrota_desafio_activity)
        //variablesIntent
        nivel = intent.getIntExtra("numeroNivel", 999)
        nombre = intent.getStringExtra("nombre")!!
        notasHacertadas = intent.getIntExtra("notasHacertadas", 0)
        tiempoDurado = intent.getIntExtra("tiempoDurado", 0)
        dificultad = intent.getIntExtra("dificultad", 0)

        val emojis = arrayOf("😄", "😃", "😁", "😊", "😆")

        textViewResultadoTotal = findViewById(R.id.textViewResultadoTotal)
        textViewResultadoTiempo = findViewById(R.id.textViewResultadoTiempo)
        emogiTextView = findViewById(R.id.emogiTextView)
        finTextView = findViewById(R.id.desafioTextView)

        var tiempo = ((tiempoDurado-60)*-1)

        //Colorar datos
        emogiTextView.text = emojis.random().toString()
        textViewResultadoTotal.text = "Total: $notasHacertadas"
        textViewResultadoTiempo.text = "Tiempo: $tiempo s"
        Utils.degradadoTexto(this, finTextView.id, R.color.morado, R.color.negro)

        //sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

       //sonido Finalizar
        mediaPlayer = MediaPlayer.create(this, R.raw.desafio_finish_sound)
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.start()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_pantallas_fin)
        imageView.startAnimation(anim)

        actualizarDatosInterfaz()
    }

    /**
     * Método llamado para actualizar la interfaz
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun actualizarDatosInterfaz() = runBlocking {
        val listaPuntuaciones = UtilsDB.getPuntuacionDesafio()?.toMutableList() ?: mutableListOf()


        val nuevoElemento = mapOf(
            "nombre" to nombre,
            "dificultad" to dificultad,
            "notas" to notasHacertadas,
            "tiempo" to (60 - tiempoDurado)
        )
        listaPuntuaciones.add(nuevoElemento)
        UtilsDB.setPuntuacionDesafio(listaPuntuaciones)
        UtilsDB.setPuntuacionDesafioGlobal(nuevoElemento)
    }


    /**
     * Método llamado al presionar el botón para repetir el desafío.
     * Reproduce un sonido y redirige al juego musical con el mismo desafío y dificultad.
     *
     * @param view Vista del botón que activa la función.
     */
    fun irRepetir(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("desafio", true)
        intent.putExtra("dificultad", dificultad)
        finish()
        startActivity(intent)
    }

    /**
     * Método llamado al presionar el botón para regresar al menú principal.
     * Reproduce un sonido y finaliza la actividad.
     *
     * @param view Vista del botón que activa la función.
     */
    fun irMenu(view: View) {
        mediaPlayer.start()
        finish()
    }
}