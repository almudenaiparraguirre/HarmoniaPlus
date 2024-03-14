package com.mariana.harmonia.activitys

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.mariana.harmonia.R
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

import kotlin.random.Random

/**
 * Actividad que representa el perfil del usuario.
 */
class NivelesAventuraActivity : AppCompatActivity() {

    private val numCantNiveles = 100
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private var nivelActual: Int = 0
    private lateinit var menuSuperior: LinearLayout
    private lateinit var textViewNivel: TextView
    private lateinit var corazonesTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var scrollView: ScrollView
    private lateinit var imagenPerfil: ImageView
    var storage = FirebaseDB.getInstanceStorage()

    /**
     * Inicialización de la actividad.
     */
    public override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveles_aventura)

        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        llBotonera = findViewById(R.id.llBotonera)
        botonCorrecto = Random.nextInt(numCantNiveles)
        menuSuperior = findViewById(R.id.llTopBar)
        textViewNivel = findViewById(R.id.textViewNivel)
        scrollView = findViewById(R.id.scrollView)
        corazonesTextView = findViewById(R.id.numeroCorazones)
        imagenPerfil = findViewById(R.id.imagenPerfil)
        inicializarConBase()
        crearCirculos()
        colocarTextViewNivel()
        downloadImage2()
        //oculta la barra de carga
        EligeModoJuegoActivity.instance.ocultarFragmento()
    }

    /**
     * Inicializa datos básicos.
     */
    private fun inicializarConBase()= runBlocking {
        nivelActual = UtilsDB.getNivelActual()!!
        corazonesTextView.text = UtilsDB.getVidas().toString()
    }

    /**
     * Descarga la imagen de perfil del usuario desde el storage.
     */
    private fun downloadImage2() {
        val storageRef = storage.reference
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

        imagesRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(this)
                .load(url)
                .into(imagenPerfil)

        }.addOnFailureListener { exception ->
            println("Error al cargar la imagen: ${exception.message}")
        }
    }

    /**
     * Crea los círculos de los botones de nivel.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun crearCirculos() {
        val lp = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.button_width),
            resources.getDimensionPixelSize(R.dimen.button_height)
        )

        lp.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin_top),
            0,
            resources.getDimensionPixelSize(R.dimen.button_margin)
        )

        for (i in 1 until numCantNiveles) {
            val button: View = if (i > nivelActual) {
                createLockedButton()
            } else {
                createUnlockedButton(i)
            }

            val lp = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.button_width),
                resources.getDimensionPixelSize(R.dimen.button_height)
            )

            val randomMargin = Random.nextInt(0, resources.getDimensionPixelSize(R.dimen.max_margin))
            lp.setMargins(
                if (i % 2 == 0) randomMargin else 0,
                resources.getDimensionPixelSize(R.dimen.button_margin_top),
                if (i % 2 != 0) randomMargin else 0,
                lp.bottomMargin
            )

            if (i > nivelActual) {
                button.isEnabled = false
                button.setBackgroundResource(getRandomUnlockedButtonDrawable())
            } else {
                button.setBackgroundResource(getRandomButtonDrawable())
                button.setOnTouchListener { view, motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f)
                            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f)
                            AnimatorSet().apply {
                                play(scaleX).with(scaleY)
                                duration = 300
                                start()
                            }
                            false
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
                            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)
                            AnimatorSet().apply {
                                play(scaleX).with(scaleY)
                                duration = 300
                                start()
                            }
                            false
                        }
                        else -> false
                    }
                }
                button.setOnClickListener {
                    mediaPlayer.start()
                    val numeroNivel = button.id
                    clickBotonNivel(numeroNivel)
                }
            }

            lp.gravity = Gravity.CENTER
            button.layoutParams = lp
            llBotonera.addView(button)
        }

        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val maxScroll = scrollView.getChildAt(0).height - scrollView.height
            val ratio = scrollY.toFloat() / maxScroll.toFloat()
            val color = interpolateColor(Color.WHITE, Color.MAGENTA, ratio)
            scrollView.setBackgroundColor(color)
        }
    }


    /**
     * Click a un nivel
     */
    private fun clickBotonNivel(numeroNivel: Int) {
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", numeroNivel)
        startActivity(intent)
        finish()
    }

    /**
     * Crea los niveles bloqueados
     */
    fun createLockedButton(): Button {
        //mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        val lockedButton = Button(this)
        lockedButton.textSize = 20f
        lockedButton.gravity = Gravity.CENTER
        lockedButton.isEnabled = false

        lockedButton.setBackgroundResource(R.drawable.style_round_button_blue)
        lockedButton.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        // Establecer el margen del texto para centrar horizontalmente el icono
        val textMargin = resources.getDimensionPixelSize(R.dimen.text_margin)
        lockedButton.setPadding(0, 0, textMargin, 0)

        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_width)
        val strokeColor = ContextCompat.getColor(this, android.R.color.white)
        val shapeDrawable = ContextCompat.getDrawable(this, R.drawable.style_round_button_blue)!!.mutate()
        shapeDrawable.setTint(strokeColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (lockedButton.background as GradientDrawable).setStroke(strokeWidth, strokeColor)
        }

        val drawableLock = ContextCompat.getDrawable(this, R.drawable.lock) // Reemplaza R.drawable.lock con tu propio recurso de icono de candado

        // Ajustar la posición del icono de candado en el botón para centrarlo horizontalmente
        drawableLock?.setBounds(0, 0, drawableLock.intrinsicWidth, drawableLock.intrinsicHeight)
        lockedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableLock, null)

        // Establecer el fondo y el borde del botón bloqueado
        lockedButton.background = shapeDrawable

        return lockedButton
    }

    /**
     * Crea los niveles desbloqueados
     */
    fun createUnlockedButton(levelNumber: Int): Button {
        val button = Button(this)
        button.textSize = 20f
        button.gravity = Gravity.CENTER

        button.id = levelNumber
        button.text = String.format("%2d", levelNumber)
        button.setBackgroundResource(getRandomButtonDrawable())
        button.setOnClickListener {
            val numeroNivel = button.id
            val intent = Intent(this, JuegoMusicalActivity::class.java)
            intent.putExtra("numeroNivel", numeroNivel)
            startActivity(intent)
        }
        val size = resources.getDimensionPixelSize(R.dimen.button_size)
        val params = LinearLayout.LayoutParams(size, size)
        button.layoutParams = params
        return button
    }

    /**
     *  Pone el estilo de desbloqueado
     */
    private fun getRandomButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }

    /**
     * Pone el estilo de bloqueado
     */
    private fun getRandomUnlockedButtonDrawable(): Int {
        val buttonDrawables = listOf(
            R.drawable.style_round_button_blue
        )
        return buttonDrawables[Random.nextInt(buttonDrawables.size)]
    }

    /**
     * Obtiene el archivo JSON de los niveles.
     */
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

    /**
     * Coloca el texto del nivel en el TextView correspondiente.
     */
    private fun colocarTextViewNivel(){
        var nivel  = nivelActual.toString()
        textViewNivel.text = "Nv. $nivel-$numCantNiveles"
    }

    /**
     * Interpola colores.
     */
    private fun interpolateColor(colorStart: Int, colorEnd: Int, ratio: Float): Int {
        val colors = intArrayOf(
            Color.parseColor("#FFDEF7"), // Rosa claro y suave
            Color.parseColor("#ffc5f1"), // Rosa claro y suave
            Color.parseColor("#ffc5e3"), // Rosa claro y suave
            Color.parseColor("#ffc5d4"), // Rosa claro y suave
            Color.parseColor("#ffc5c6"), // Rosa claro y suave
            Color.parseColor("#ffd3c5"), // Rosa claro y suave
            Color.parseColor("#ffe2c5"),
            Color.parseColor("#fff1c5"),
            Color.parseColor("#ffffc5"),
            Color.parseColor("#f1ffc5"),
            Color.parseColor("#e2ffc5"),
            Color.parseColor("#d4ffc5"),
            Color.parseColor("#c6ffc5"),
            Color.parseColor("#c5ffd3"),
            Color.parseColor("#c5ffe2"),
            Color.parseColor("#c5fff0"),
            Color.parseColor("#c5ffff"),
        )

        val startIndex = (ratio * (colors.size - 1)).toInt()
        val endIndex = minOf(startIndex + 1, colors.size - 1)
        val startColor = colors[startIndex]
        val endColor = colors[endIndex]

        val startRatio = 1 - (ratio * (colors.size - 1) - startIndex)
        val endRatio = 1 - startRatio

        val r = (Color.red(startColor) * startRatio + Color.red(endColor) * endRatio).toInt()
        val g = (Color.green(startColor) * startRatio + Color.green(endColor) * endRatio).toInt()
        val b = (Color.blue(startColor) * startRatio + Color.blue(endColor) * endRatio).toInt()

        return Color.rgb(r, g, b)
    }

    /**
     * Volver al modo de juego
     */
    fun clickAtras(view: View){
        mediaPlayer.start()
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Volver al modo de juego
     */
    fun volverModoJuego(view: View){
        mediaPlayer.start()
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Ir al perfil del usuario
     */
    fun verPerfilUsuario(view: View){
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Maneja el evento de clic en el botón de retroceso.
     */
    override fun onBackPressed() {
        EligeModoJuegoActivity.instance.inicializarConBase()
        super.onBackPressed()
    }
}