package com.mariana.harmonia.activities

import com.mariana.harmonia.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random


class NivelesAventuraActivity : AppCompatActivity() {

    private var numCantNiveles = 100
    private var vidas = 0
    private lateinit var llBotonera: LinearLayout
    private var botonCorrecto: Int = 0
    private var nivelActual: Int = 0
    private lateinit var menuSuperior: LinearLayout
    private lateinit var textViewNivel: TextView
    private lateinit var corazonesTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var scrollView: ScrollView
    private lateinit var imagenPerfil: ImageView
    private lateinit var imageViewFondo1: ImageView
    private lateinit var imageViewFondo2: ImageView
    private lateinit var imageViewFondo3: ImageView
    private lateinit var imageViewFondo4: ImageView
    private lateinit var imageViewFondo5: ImageView
    private lateinit var imageViewFondo6: ImageView
    private lateinit var imageViewFondo7: ImageView
    private lateinit var imageViewFondo8: ImageView

    var storage = FirebaseDB.getInstanceStorage()

    override fun onCreate(savedInstanceState: Bundle?) {
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
        imageViewFondo1 = findViewById(R.id.imageViewFondo1)
        imageViewFondo2 = findViewById(R.id.imageViewFondo2)
        imageViewFondo3 = findViewById(R.id.imageViewFondo3)
        imageViewFondo4 = findViewById(R.id.imageViewFondo4)
        imageViewFondo5 = findViewById(R.id.imageViewFondo5)
        imageViewFondo6 = findViewById(R.id.imageViewFondo6)
        imageViewFondo7 = findViewById(R.id.imageViewFondo7)
        imageViewFondo8 = findViewById(R.id.imageViewFondo8)

        inicializarConBase()
        crearCirculos()
        colocarTextViewNivel()
        downloadImage2()
        EligeModoJuegoActivity.instance.ocultarFragmento()
        corazonesTextView.text = vidas.toString()

        // Configurar el listener de desplazamiento del ScrollView
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            handleScrollChange(scrollY)
        }

        val unlockedLevel: Int = nivelActual
        scrollToUnlockedLevel(unlockedLevel)
    }

    fun scrollToUnlockedLevel(unlockedLevel: Int) {
        scrollView.post {
            val targetView = llBotonera.getChildAt(unlockedLevel)
            if (targetView != null) {
                val targetScrollY = targetView.top + targetView.height / 2 - scrollView.height / 2

                // Usando ObjectAnimator para animar la propiedad scrollY
                ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.scrollY, targetScrollY).apply {
                    duration = 1000 // Duración en milisegundos, ajusta a tu preferencia
                    start()
                }
            }
        }
    }

    private fun inicializarConBase() = runBlocking {
        setVidas()
        nivelActual = UtilsDB.getNivelMaximo()!!
        vidas = UtilsDB.getVidas()!!.toInt()
    }

    private fun setVidas() = runBlocking {
        var ultimoTiempo = UtilsDB.getUltimoTiempo()
    }

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
                    if (vidas <= 0) {
                        noVidas()
                    } else {
                        mediaPlayer.start()
                        val numeroNivel = button.id
                        clickBotonNivel(numeroNivel)
                    }
                }
            }

            lp.gravity = Gravity.CENTER
            button.layoutParams = lp
            llBotonera.addView(button)
        }
    }

    private fun noVidas() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("No tienes vidas suficientes para jugar")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun clickBotonNivel(numeroNivel: Int) {
        val intent = Intent(this, JuegoMusicalActivity::class.java)
        intent.putExtra("numeroNivel", numeroNivel)
        startActivity(intent)
        finish()
    }

    fun createLockedButton(): Button {
        val lockedButton = Button(this)
        lockedButton.textSize = 20f
        lockedButton.gravity = Gravity.CENTER
        lockedButton.isEnabled = false

        lockedButton.setBackgroundResource(R.drawable.style_round_button_blue)
        lockedButton.setTextColor(ContextCompat.getColor(this, android.R.color.white))

        val textMargin = resources.getDimensionPixelSize(R.dimen.text_margin)
        lockedButton.setPadding(0, 0, textMargin, 0)

        val strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_width)
        val strokeColor = ContextCompat.getColor(this, android.R.color.white)
        val shapeDrawable = ContextCompat.getDrawable(this, R.drawable.style_round_button_blue)!!.mutate()
        shapeDrawable.setTint(strokeColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (lockedButton.background as GradientDrawable).setStroke(strokeWidth, strokeColor)
        }

        val drawableLock = ContextCompat.getDrawable(this, R.drawable.lock)
        drawableLock?.setBounds(0, 0, drawableLock.intrinsicWidth, drawableLock.intrinsicHeight)
        lockedButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableLock, null)

        lockedButton.background = shapeDrawable

        return lockedButton
    }

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

    private fun colocarTextViewNivel(){
        var nivel = nivelActual.toString()
        textViewNivel.text = "Nv. $nivel-$numCantNiveles"
    }

    private fun handleScrollChange(scrollY: Int) {
        val maxScroll = scrollView.getChildAt(0).height - scrollView.height
        val ratio = scrollY.toFloat() / maxScroll.toFloat()
        val segment = 1.0f / 7.0f // 7 transiciones para 8 imágenes

        imageViewFondo1.alpha = calculateAlpha(ratio, 0 * segment, 1 * segment)
        imageViewFondo2.alpha = calculateAlpha(ratio, 1 * segment, 2 * segment)
        imageViewFondo3.alpha = calculateAlpha(ratio, 2 * segment, 3 * segment)
        imageViewFondo4.alpha = calculateAlpha(ratio, 3 * segment, 4 * segment)
        imageViewFondo5.alpha = calculateAlpha(ratio, 4 * segment, 5 * segment)
        imageViewFondo6.alpha = calculateAlpha(ratio, 5 * segment, 6 * segment)
        imageViewFondo7.alpha = calculateAlpha(ratio, 6 * segment, 7 * segment)
        imageViewFondo8.alpha = calculateAlpha(ratio, 7 * segment, 8 * segment)
    }

    private fun calculateAlpha(ratio: Float, start: Float, end: Float): Float {
        return when {
            ratio < start -> 0f
            ratio > end -> 1f
            else -> (ratio - start) / (end - start)
        }
    }

    fun clickAtras(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun volverModoJuego(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun verPerfilUsuario(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        EligeModoJuegoActivity.instance.inicializarConBase()
        super.onBackPressed()
    }
}