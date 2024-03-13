package com.mariana.harmonia.activitys

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color


import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R
import com.mariana.harmonia.activitys.pantallasExtras.derrotaDesafio_activity
import com.mariana.harmonia.activitys.pantallasExtras.derrota_activity
import com.mariana.harmonia.activitys.pantallasExtras.victoria_activity
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

/**
 * Actividad principal para el juego musical.
 *
 * Esta actividad maneja la lógica y la interfaz gráfica del juego musical, donde el usuario
 * debe tocar las notas correctas en el momento adecuado. Puede ser un nivel regular o un desafío.
 *
 * @property imagenNota ImageView que muestra la imagen de la nota musical.
 * @property textViewNota TextView que muestra el nombre de la nota musical.
 * @property contadorTextView TextView que muestra el contador de aciertos.
 * @property tituloTextView TextView que muestra el título del nivel o desafío.
 * @property contadorVidas TextView que muestra el contador de vidas restantes.
 * @property textViewTiempo TextView que muestra el tiempo restante.
 * @property textViewAccuracy TextView que muestra la precisión del jugador.
 * @property tiempoProgressBar ProgressBar que indica el tiempo restante.
 * @property imagenProgressBar ImageView que muestra la imagen de progreso.
 * @property timer CountDownTimer utilizado para el contador de tiempo.
 * @property animacionTexto AnimatorSet para la animación de texto.
 * @property perdido Indica si el jugador ha perdido.
 * @property ganado Indica si el jugador ha ganado.
 * @property desafio Indica si el juego es un desafío.
 * @property entradoCuentaAtras Indica si se ha activado la cuenta atrás.
 * @property nivel Número del nivel actual.
 * @property intentos Número de intentos realizados por el jugador.
 * @property aciertos Número de aciertos del jugador.
 * @property tiempo Tiempo total del juego en segundos.
 * @property tiempoActual Tiempo actual en segundos.
 * @property dificultad Nivel de dificultad del juego.
 * @property notasTotales Número total de notas en el juego.
 * @property vidas Número de vidas del jugador.
 * @property notasArray Array que contiene las notas del juego.
 * @property handler Manejador de hilos para el contador de tiempo.
 * @property handler2 Manejador de hilos adicional.
 * @property countDownTimer Contador de tiempo para la cuenta regresiva.
 */
class JuegoMusicalActivity : AppCompatActivity() {

    private lateinit var imagenNota: ImageView
    private lateinit var textViewNota: TextView
    private lateinit var contadorTextView: TextView
    private lateinit var tituloTextView: TextView
    private lateinit var contadorVidas: TextView
    private lateinit var textViewTiempo: TextView
    private lateinit var textViewAccuracy: TextView
    private lateinit var tiempoProgressBar: ProgressBar
    private lateinit var imagenProgressBar: ImageView
    private lateinit var fondo: ImageView
    private lateinit var pentagrama: RelativeLayout


    private lateinit var timer: CountDownTimer // Timer para contar hacia atrás
    var animacionTexto: AnimatorSet? = null
    private var perdido: Boolean = false
    private var ganado: Boolean = false
    private var desafio: Boolean = false
    private var entradoCuentaAtras: Boolean = false

    private var nivel: Int? = 1
    private var intentos: Int? = 0
    private var aciertos: Int? = 0
    private var tiempo: Double? = 60.0
    private var tiempoActual: Int? = 0
    private var dificultad: Int? = 0

    private var notasTotales: Int? = 0
    private var vidas: Int? = 0
    private lateinit var notasArray: Array<String?>

    //manejadores contadores
    private val handler = Handler(Looper.getMainLooper())
    private val handler2 = Handler(Looper.getMainLooper())
    private var countDownTimer: CountDownTimer? = null

    /**
     * Método que se llama al crear la actividad.
     *
     * Aquí se inicializan los elementos de la interfaz gráfica, se configuran los listeners,
     * se cargan los datos del nivel o desafío, y se inician los contadores de tiempo.
     */
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.juego_musical_activity)

        desafio = intent.getBooleanExtra("desafio", false)
        dificultad = intent.getIntExtra("dificultad", 0)


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
        textViewNota = findViewById(R.id.layoutTexto)
        contadorTextView = findViewById(R.id.contadorTextView)
        tituloTextView = findViewById(R.id.tituloTextView)
        contadorVidas = findViewById(R.id.textViewCorazones)
        tiempoProgressBar = findViewById<ProgressBar>(R.id.tiempoProgressBar)
        imagenProgressBar = findViewById(R.id.imageMarker)
        textViewTiempo = findViewById(R.id.textViewTiempoContador)
        textViewAccuracy = findViewById(R.id.textViewAccuracy)
        fondo = findViewById(R.id.fondoFlash)
        pentagrama = findViewById(R.id.layoutPentagrama)

        //Admin del tiempo

        if (!desafio) {
            nivel = intent.getIntExtra("numeroNivel", 1)
            cargarDatosDelNivel(nivel!!)
            //Condiciones iniciales
            cambiarImagen(notasArray[0].toString())
            cambiarTexto("...")
            actualizarDatosInterfaz()
            iniciarCuentaRegresiva()
            iniciarContador()
        } else {
            nivel = intent.getIntExtra("numeroNivel", 999)
            cargarDatosDesafio()
            cambiarImagen(notasArray[0].toString())
            cambiarTexto("...")
            actualizarDatosInterfazDesafio()
            iniciarCuentaRegresiva()
            iniciarContador()
        }

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

    /**
     * Método para iniciar el contador de tiempo con cuenta regresiva.
     */
    fun iniciarContador() {

        val countDownTimer = object : CountDownTimer(
            (tiempo!! * 1000 + 1000).toLong(),
            1000
        ) { // Conteo desde 100 hasta 0 en intervalos de 1 segundo
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                cambiarTiempo(secondsLeft.toInt())

                if (secondsLeft <= 4 && !entradoCuentaAtras) {
                    animacionCuentaRegresiva()
                    entradoCuentaAtras = true
                }
            }

            override fun onFinish() {
                cambiarTiempo(0)
            }
        }

        // Inicia el contador
        countDownTimer.start()

    }

    /**
     * Método para iniciar la cuenta regresiva del tiempo.
     */
    fun iniciarCuentaRegresiva() {

        val intervalo = 10L // Intervalo de actualización en milisegundos (10ms)
        val duracionTotal = tiempo!! * 1000L // Duración total en milisegundos (1000ms = 1 segundo)
        val decrementoPorIntervalo =
            1.0 * intervalo / 1000.0 // Cantidad de tiempo que se decrementa en cada intervalo

        handler.postDelayed(object : Runnable {

            override fun run() {
                tiempo = tiempo!! - decrementoPorIntervalo

                // Calcular el progreso actual de la barra con números decimales
                val progresoActual =
                    ((tiempo!! * 1000).toFloat() / duracionTotal.toFloat() * 1000).toInt()
                tiempoProgressBar.progress = progresoActual
                println(progresoActual)

                if (!desafio) {
                    isPerdido()
                } else {
                    isPerdidoDesafio()
                }
                // Decrementar el tiempo

                if (tiempo!! > 0) {
                    handler.postDelayed(this, intervalo)
                }
            }
        }, intervalo)

    }
    fun flashAcierto() {


        // Animación del fondo de blanco a verde en 0.1 segundos
        val colorAnimator = ObjectAnimator.ofArgb(pentagrama, "backgroundColor", Color.WHITE, Color.GREEN)
        colorAnimator.duration = 100 // Duración de 0.1 segundos (en milisegundos)

        // Animación del alpha del pentagrama
        val alphaAnimator = ObjectAnimator.ofFloat(fondo, "alpha", 0f, 1f)
        alphaAnimator.duration = 100 // Duración de 0.1 segundos (en milisegundos)

        // Animación del desplazamiento hacia abajo del fondo en 0.4 segundos
        val translateYAnimator = ObjectAnimator.ofFloat(fondo, "translationY", 0f, 200f)
        translateYAnimator.duration = 400 // Duración de 0.4 segundos (en milisegundos)

        // Combinar las animaciones
        colorAnimator.start()
        alphaAnimator.start()
        translateYAnimator.start()

        // Espera 0.1 segundos antes de revertir las animaciones
        fondo.postDelayed({
            // Animación del fondo de verde a blanco en 0.3 segundos
            val colorAnimatorReverse = ObjectAnimator.ofArgb(pentagrama, "backgroundColor", Color.GREEN, Color.WHITE)
            colorAnimatorReverse.duration = 300 // Duración de 0.3 segundos (en milisegundos)

            // Animación del alpha del pentagrama (volver a 0)
            val alphaAnimatorReverse = ObjectAnimator.ofFloat(fondo, "alpha", 1f, 0f)
            alphaAnimatorReverse.duration = 300 // Duración de 0.3 segundos (en milisegundos)

            // Animación para que el fondo regrese a su posición inicial de manera instantánea
            val translateBackAnimator = ObjectAnimator.ofFloat(fondo, "translationY", 100f, 0f)
            translateBackAnimator.duration = 0 // Instantáneo

            // Combinar las animaciones de reversión
            colorAnimatorReverse.start()
            alphaAnimatorReverse.start()
            translateBackAnimator.start()
        }, 100)
    }
    /**
     * Método para realizar la animación de la cuenta regresiva.
     */
    private fun animacionCuentaRegresiva() {
        var mediaPlayer = MediaPlayer.create(this, R.raw.sound_cuenta_atras)
        mediaPlayer.start()
        var imageViewRojo: ImageView = findViewById<ImageView>(R.id.imageViewProgressBarRoja)
        val latidoAnimation = AnimatorInflater.loadAnimator(
            this,
            R.animator.cuenta_regresiva_animation
        ) as AnimatorSet

        // Establecer el objetivo de la animación (ImageView)
        latidoAnimation.setTarget(imageViewRojo)

        // Iniciar la animación
        latidoAnimation.start()
    }


    /**
     * Método para cambiar el tiempo restante en la interfaz.
     *
     * @param segundos Segundos restantes.
     */
    public fun cambiarTiempo(segundos: Int) {
        tiempoActual = segundos
        if (segundos > 1000) {
            textViewTiempo.text = ""
        } else {
            textViewTiempo.text = segundos.toString() + "s"
        }
    }

    /**
     * Actualiza la interfaz gráfica con la información actualizada.
     * Calcula las vidas restantes, establece textos y realiza actualizaciones según el estado del juego.
     */
    private fun actualizarDatosInterfaz() {
        var vidasTotales = vidas!! - (intentos!! - aciertos!!)
        notasTotales = notasArray.size
        contadorTextView.text = "$aciertos/$notasTotales"
        tituloTextView.text = "Nivel-$nivel"
        if (vidasTotales!! <= 10) {
            contadorVidas.text = "X$vidasTotales"
        } else {
            contadorVidas.text = "X∞"
        }
    }

    /**
     * Comprueba la jugada del usuario y realiza acciones correspondientes en caso de acierto o fallo.
     * Actualiza la interfaz gráfica y verifica si el usuario ha ganado.
     *
     * @param nombreNota Nombre de la nota seleccionada por el usuario.
     */
    private fun comprobarJugada(nombreNota: String) {
        // Sumamos una a intentos
        intentos = intentos?.plus(1)
        // Comprobamos si el array se ha terminado
        if (aciertos != null && aciertos!! < notasArray.size) {
            // Si la nota es la indicada entra
            if (notasArray[aciertos!!]?.substring(1) == nombreNota) {
                var nota = notasArray[aciertos!!]
                playSound("sound_$nota")
                println("pasadoPlaySound")
                aciertos = aciertos?.plus(1)


                // Comprobar si aciertos sigue siendo menor que la longitud de notasArray
                if (aciertos!! < notasArray.size) {
                    cambiarImagen(notasArray[aciertos!!].toString())
                }
                cambiarTexto(nombreNota)
                if (!desafio) {
                    actualizarDatosInterfaz()
                } else {
                    actualizarDatosInterfazDesafio()
                }
                animacionAcierto()
                isGanado()

            } else {
                //si es la ultima vida no hace animacion

                animacionFallo()
                animacionPerdidaCorazon()
                quitarVida()

            }
            ponerAccuracy()
        }
    }

    /**
     * Disminuye el contador de vidas y actualiza la interfaz gráfica con la cantidad restante.
     */
    private fun quitarVida() {
        var vidasTotales = vidas!! - (intentos!! - aciertos!!)!!
        if (vidasTotales >= 0) {
            if (vidasTotales!! < 10) {
                contadorVidas.text = "X$vidasTotales"
            } else {
                contadorVidas.text = "X∞"
            }
        }
    }

    /**
     * Calcula y devuelve el porcentaje de precisión basado en los aciertos e intentos.
     *
     * @return Porcentaje de precisión.
     */
    private fun getAccuracy(): Int {
        var accuracyPercentage = 0
        if (aciertos!! > 0 && intentos!! > 0) {
            val aciertosDouble = aciertos!!.toDouble()
            val intentosDouble = intentos!!.toDouble()
            val accuracy = aciertosDouble / intentosDouble
            accuracyPercentage = (accuracy * 100).toInt()
        }
        return accuracyPercentage
    }

    /**
     * Actualiza el texto de precisión en la interfaz gráfica.
     */
    private fun ponerAccuracy() {
        var accuracy = getAccuracy()
        textViewAccuracy.text = "$accuracy%"
    }

    /**
     * Verifica si el jugador ha perdido y, en ese caso, inicia la pantalla de derrota.
     */
    private fun isPerdido() {
        var vidasTotales = vidas!! - (intentos!! - aciertos!!)!!
        if ((vidas!! - (intentos!! - aciertos!!)!! <= 0 || tiempoProgressBar.progress <= 0) && !perdido) {
            perdido()
        }
    }

    /**
     * Inicia la actividad de derrota y pasa información relevante como el número de nivel.
     */
    private fun perdido() {
        val intent = Intent(this, derrota_activity::class.java)
        intent.putExtra("numeroNivel", nivel)
        perdido = true;
        finish()
        startActivity(intent)
    }

    /**
     * Verifica si el jugador ha perdido en el modo desafío y, en caso afirmativo, inicia la pantalla de derrota.
     */
    private fun isPerdidoDesafio() {
        var vidasTotales = vidas!! - (intentos!! - aciertos!!)!!
        if ((vidas!! - (intentos!! - aciertos!!)!! <= 0 || tiempoProgressBar.progress <= 0) && !perdido) {
            perdidoDesafio()
        }
    }

    /**
     * Inicia la actividad de derrota en modo desafío y pasa información relevante como el número de nivel y estadísticas.
     */
    private fun perdidoDesafio() {
        tituloTextView.text = "Has perdido"
        val intent = Intent(this, derrotaDesafio_activity::class.java)
        intent.putExtra("numeroNivel", nivel)
        intent.putExtra("notasHacertadas", aciertos)
        intent.putExtra("tiempoDurado", tiempoActual)
        intent.putExtra("dificultad", dificultad)
        perdido = true;
        finish()
        startActivity(intent)
    }

    /**
     * Verifica si el jugador ha ganado y, en caso afirmativo, inicia la pantalla de victoria.
     */
    private fun isGanado() {
        if (aciertos == notasTotales) {
            ganado()
        }
    }

    /**
     * Inicia la actividad de victoria y pasa información relevante como el número de nivel y precisión.
     */
    private fun ganado() {
        val intent = Intent(this, victoria_activity::class.java)
        intent.putExtra("numeroNivel", nivel)
        intent.putExtra("precision", getAccuracy()) // Aquí pasas la segunda variable
        ganado = true

        pararConadores()
        finish()
        startActivity(intent)
    }

    /**
     * Detiene los contadores y la cuenta regresiva cuando se alcanza la victoria.
     */
    private fun pararConadores() {
        detenerContador()
        detenerCuentaRegresiva()
        ganado = true
    }

    /**
     * Cambia el texto de la nota en la interfaz gráfica según la nota seleccionada.
     *
     * @param texto Nombre de la nota a mostrar.
     */
    private fun cambiarTexto(texto: String) {

        textViewNota.text = traducirNota(texto)
    }

    /**
     * Cambia la imagen de la nota en la interfaz gráfica según el nombre del archivo de la nota.
     *
     * @param nombreArchivo Nombre del archivo de la nota.
     */
    private fun cambiarImagen(nombreArchivo: String) {
        val idImagen = resources.getIdentifier("nota_" + nombreArchivo, "drawable", packageName)
        if (idImagen != 0) {
            imagenNota.setImageResource(idImagen)
        }
    }


    /**
     * Realiza la animación de acierto, incluyendo cambio de color, sonido y animación de zoom.
     */
    fun animacionAcierto() {
        // Cambiar color a verde instantáneamente
        playSound("correcto")
        flashAcierto()
        textViewNota.setTextColor(Color.GREEN)
        // Animación para volver al color original con desvanecido
        val animacion = ObjectAnimator.ofArgb(textViewNota, "textColor", Color.GREEN, Color.BLACK)
        animacion.duration = 1000
        animacion.start()
        val AnimacionZoom =
            AnimatorInflater.loadAnimator(this, R.animator.acierto_nota_color) as AnimatorSet
        // Establecer el objetivo de la animación (ImageView)
        AnimacionZoom.setTarget(textViewNota)
        // Iniciar la animación
        AnimacionZoom.start()


        handler2.removeCallbacksAndMessages(null) // Eliminar cualquier llamada pendiente
        handler2.postDelayed({
            cambiarTexto("...")
        }, 1000)

    }

    /**
     * Realiza la animación de fallo, incluyendo cambio de color y sonido.
     */
    fun animacionFallo() {
        // Cambiar color a verde instantáneamentes
        playSound("incorrecto")
        textViewNota.setTextColor(Color.RED)

        // Animación para volver al color original con desvanecido
        val animacion = ObjectAnimator.ofArgb(textViewNota, "textColor", Color.RED, Color.BLACK)
        animacion.duration = 1000
        animacion.start()


    }

    /**
     * Reproduce un sonido específico.
     *
     * @param soundFile Nombre del archivo de sonido.
     */
    private fun playSound(soundFile: String) {
        val mediaPlayer =
            MediaPlayer.create(this, resources.getIdentifier(soundFile, "raw", packageName))
        val volume = 1f
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }

    /**
     * Actualiza el fondo de las teclas negras en la interfaz gráfica.
     *
     * @param viewId ID de la vista a la que se le actualizará el fondo.
     * @param drawableId ID del recurso drawable que se utilizará como fondo.
     */
    private fun actualizarFondoNegras(viewId: Int, drawableId: Int) {
        val view = findViewById<View>(viewId)
        val drawable: Drawable? = ContextCompat.getDrawable(this, drawableId)
        drawable?.let {
            view.background = it
        }
    }

    /**
     * Actualiza el fondo de las teclas blancas en la interfaz gráfica.
     *
     * @param imageViewId ID del ImageView al que se le actualizará la imagen.
     * @param nuevaImagenId ID del recurso drawable que se utilizará como nueva imagen.
     * @param actividad Actividad que contiene la vista.
     */
    fun actualizarFondoBlancas(imageViewId: Int, nuevaImagenId: Int, actividad: AppCompatActivity) {
        // Buscar el ImageView por su ID
        val imageView = actividad.findViewById<ImageView>(imageViewId)
        // Establecer la nueva imagen
        imageView.setImageResource(nuevaImagenId)
    }


    /**
     * Traduce el nombre de una nota al formato legible.
     *
     * @param nota Nombre de la nota a traducir.
     * @return Nombre de la nota traducido.
     */
    fun traducirNota(nota: String): String {
        return when (nota) {
            "c" -> "DO"
            "d" -> "RE"
            "e" -> "MI"
            "f" -> "FA"
            "g" -> "SOL"
            "a" -> "LA"
            "b" -> "SI"
            "db" -> "RE♭"
            "eb" -> "MI♭"
            "gb" -> "SOL♭"
            "ab" -> "LA♭"
            "bb" -> "SI♭"
            else -> "..."
        }
    }

    /**
     * Maneja el clic en la nota Do.
     */
    private fun clickDo() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Do")
        actualizarFondoBlancas(R.id.notaDo, R.drawable.svg_tecla_do_clicada, this)
        cambiarImagen("nota_6d")
        comprobarJugada("c")
    }

    /**
     * Maneja el clic en la nota Re_b.
     */
    fun clickRe_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Reb")
        actualizarFondoNegras(R.id.fondoReB, R.drawable.style_buttond_degradado_suave)
        comprobarJugada("db")
    }

    /**
     * Maneja el clic en la nota Re.
     */
    private fun clickRe() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Re")
        actualizarFondoBlancas(R.id.notaRe, R.drawable.svg_tecla_re_clicada, this)
        comprobarJugada("d")
    }

    /**
     * Maneja el clic en la nota Mib.
     */
    fun clickMi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Mib")
        actualizarFondoNegras(R.id.fondoMiB, R.drawable.style_buttond_degradado_suave)
        comprobarJugada("eb")
    }

    /**
     * Maneja el clic en la nota Mi.
     */
    private fun clickMi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Mi")
        actualizarFondoBlancas(R.id.notaMi, R.drawable.svg_tecla_mi_clicada, this)
        comprobarJugada("e")
    }

    /**
     * Maneja el clic en la nota Fa.
     */
    private fun clickFa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Fa")
        actualizarFondoBlancas(R.id.notaFa, R.drawable.svg_tecla_fa_clicada, this)
        comprobarJugada("f")
    }

    /**
     * Maneja el clic en la nota Solb.
     */
    fun clickSol_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Solb")
        actualizarFondoNegras(R.id.fondoSolB, R.drawable.style_buttond_degradado_suave)
        comprobarJugada("gb")
    }

    /**
     * Maneja el clic en la nota Sol.
     */
    private fun clickSol() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Sol")
        actualizarFondoBlancas(R.id.notaSol, R.drawable.svg_tecla_sol_clicada, this)
        comprobarJugada("g")
    }

    /**
     * Maneja el clic en la nota Lab.
     */
    fun clickLa_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Lab")
        actualizarFondoNegras(R.id.fondoLaB, R.drawable.style_buttond_degradado_suave)
        comprobarJugada("ab")
    }

    /**
     * Maneja el clic en la nota La.
     */
    private fun clickLa() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota La")
        actualizarFondoBlancas(R.id.notaLa, R.drawable.svg_tecla_la_clicada, this)
        comprobarJugada("a")
    }

    /**
     * Maneja el clic en la nota Sib.
     */
    fun clickSi_b() {
        Log.d("pruebasActivity", "Se ha hecho clic en el método Sib")
        actualizarFondoNegras(R.id.fondoSiB, R.drawable.style_buttond_degradado_suave)
        comprobarJugada("bb")
    }

    /**
     * Maneja el clic en la nota Si.
     */
    private fun clickSi() {
        Log.d("pruebasActivity", "Se ha hecho clic en la nota Si")
        actualizarFondoBlancas(R.id.notaSi, R.drawable.svg_tecla_si_clicada, this)
        comprobarJugada("b")
    }

    //Soltar
    /**
     * Maneja el soltar de la nota Do.
     */
    private fun soltarDo() {
        Log.d("pruebasActivity", "Se ha soltado la nota Do")
        actualizarFondoBlancas(R.id.notaDo, R.drawable.svg_tecla_do, this)
    }

    /**
     * Maneja el soltar de la nota Reb.
     */
    private fun soltarRe_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota reB")
        actualizarFondoNegras(R.id.fondoReB, R.drawable.sytle_degradado_fondo_piano)
    }

    /**
     * Maneja el soltar de la nota Re.
     */
    private fun soltarRe() {
        Log.d("pruebasActivity", "Se ha soltado la nota Re")
        actualizarFondoBlancas(R.id.notaRe, R.drawable.svg_tecla_re, this)
    }

    /**
     * Maneja el soltar de la nota Mib.
     */
    private fun soltarMi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mib")
        actualizarFondoNegras(R.id.fondoMiB, R.drawable.sytle_degradado_fondo_piano)
    }

    /**
     * Maneja el soltar de la nota Mi.
     */
    private fun soltarMi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Mi")
        actualizarFondoBlancas(R.id.notaMi, R.drawable.svg_tecla_mi, this)
    }

    /**
     * Maneja el soltar de la nota Fa.
     */
    private fun soltarFa() {
        Log.d("pruebasActivity", "Se ha soltado la nota Fa")
        actualizarFondoBlancas(R.id.notaFa, R.drawable.svg_tecla_fa, this)
    }

    /**
     * Maneja el soltar de la nota Solb.
     */
    private fun soltarSol_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Solb")
        actualizarFondoNegras(R.id.fondoSolB, R.drawable.sytle_degradado_fondo_piano)
    }

    /**
     * Maneja el soltar de la nota Sol.
     */
    private fun soltarSol() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sol")
        actualizarFondoBlancas(R.id.notaSol, R.drawable.svg_tecla_sol, this)
    }

    /**
     * Maneja el soltar de la nota Lab.
     */
    private fun soltarLa_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Lab")
        actualizarFondoNegras(R.id.fondoLaB, R.drawable.sytle_degradado_fondo_piano)
    }

    /**
     * Maneja el soltar de la nota La.
     */
    private fun soltarLa() {
        Log.d("pruebasActivity", "Se ha soltado la nota La")
        actualizarFondoBlancas(R.id.notaLa, R.drawable.svg_tecla_la, this)
    }

    /**
     * Maneja el soltar de la nota Sib.
     */
    private fun soltarSi_b() {
        Log.d("pruebasActivity", "Se ha soltado la nota Sib")
        actualizarFondoNegras(R.id.fondoSiB, R.drawable.sytle_degradado_fondo_piano)
    }

    /**
     * Maneja el soltar de la nota Si.
     */
    private fun soltarSi() {
        Log.d("pruebasActivity", "Se ha soltado la nota Si")
        actualizarFondoBlancas(R.id.notaSi, R.drawable.svg_tecla_si, this)
    }

    /**
     * Muestra un diálogo de confirmación al intentar salir del nivel.
     */
    fun clickAtras(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        mostrarDialogoConfirmacion()
    }

    /**
     * Maneja la pulsación del botón de retroceso para mostrar el diálogo de confirmación.
     */
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        mostrarDialogoConfirmacion()
    }

    /**
     * Muestra un diálogo de confirmación al intentar salir del nivel.
     */
    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir del nivel")
        builder.setMessage("¿Estás seguro de querer salir del nivel? Perderás todos los progresos")
        builder.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            perdido = true


            finish()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Obtiene los niveles desde un archivo JSON y retorna el objeto JSON resultante.
     *
     * @return Objeto JSON que contiene la información de los niveles.
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
     * Carga los datos del nivel especificado desde el archivo JSON.
     *
     * @param nivelId ID del nivel a cargar.
     */
    private fun cargarDatosDelNivel(nivelId: Int) {

        val nivelesJson = obtenerNivelesJSON()
        val nivelesArray = nivelesJson?.getJSONArray("niveles")

        if (nivelesArray != null) {
            for (i in 0 until nivelesArray.length()) {
                val nivel = nivelesArray.getJSONObject(i)
                val id = nivel.getInt("id")

                if (id == nivelId) {
                    val completado = nivel.getBoolean("completado")
                    tiempo = nivel.getDouble("tiempo")
                    val notasJSONArray = nivel.getJSONArray("notas")
                    vidas = nivel.getInt("vidas")

                    // Convierte JSONArray a Array<String>
                    notasArray = Array(notasJSONArray.length()) { index ->
                        notasJSONArray.getString(index)
                    }

                    // Aquí puedes usar las variables según tus necesidades
                    // Por ejemplo:
                    // tiempoTextView.text = tiempo.toString()

                    break
                }
            }
        }
    }

    //metodos desafio:
    /**
     * Genera un array de notas aleatorias según la cantidad y dificultad especificadas.
     *
     * @param cantidad Cantidad de notas a generar.
     * @param dificultad Nivel de dificultad (0: Fácil, 1: Medio, 2: Difícil).
     * @return Array de notas aleatorias.
     */
    fun getNotasAleatorias(cantidad: Int, dificultad: Int): Array<String?> {
        val notasFacil = arrayOf(
            "4c",
            "4d",
            "4e",
            "4f",
            "4g",
            "4a",
            "4b",
            "5c",
        )

        val notasMedio = arrayOf(
            "3g",
            "3a",
            "3b",
            "4a",
            "4b",
            "4c",
            "4d",
            "4e",
            "4f",
            "4g",
            "5a",
            "5b",
            "5c",
            "5d",
            "5e",
            "5f",
            "5g",
            "6c",
            "6d"
        )

        val notasDificil = arrayOf(
            "3gb",
            "3g",
            "3ab",
            "3a",
            "3bb",
            "3b",
            "4c",
            "4db",
            "4d",
            "4eb",
            "4e",
            "4f",
            "4gb",
            "4g",
            "4ab",
            "4a",
            "4bb",
            "4b",
            "5c",
            "5db",
            "5d",
            "5eb",
            "5e",
            "5f",
            "5gb",
            "5g",
            "5ab",
            "5a",
            "5bb",
            "5b",
            "6c",
            "6db",
            "6d"
        )

        val notasSeleccionadas = when (dificultad) {
            0 -> notasFacil
            1 -> notasMedio
            2 -> notasDificil
            else -> throw IllegalArgumentException("Nivel de dificultad inválido")
        }

        val arrayAleatorio = arrayOfNulls<String>(cantidad)

        for (i in 0 until cantidad) {
            val notaAleatoria = notasSeleccionadas.random()
            arrayAleatorio[i] = notaAleatoria
        }

        return arrayAleatorio
    }

    /**
     * Carga datos específicos para el modo desafío.
     */
    fun cargarDatosDesafio() {
        tiempo = 60.0
        vidas = 1
        notasArray = getNotasAleatorias(1000, dificultad!!)
    }

    /**
     * Actualiza la interfaz gráfica con los datos específicos del modo desafío.
     */
    private fun actualizarDatosInterfazDesafio() {
        notasTotales = notasArray.size
        contadorTextView.text = "$aciertos/∞"
        tituloTextView.text = "DESAFÍO"
        contadorVidas.text = "1"
    }

    /**
     * Detiene el contador en el modo de juego principal.
     */
    fun detenerContador() {
        countDownTimer?.cancel()
    }

    /**
     * Detiene las animaciones y actualizaciones de la cuenta regresiva.
     */
    fun detenerCuentaRegresiva() {
        handler.removeCallbacksAndMessages(null)
        handler2.removeCallbacksAndMessages(null)
    }

    /**
     * Realiza la animación de pérdida de un corazón.
     */
    private fun animacionPerdidaCorazon() {
        // Cargar la animación desde el recurso XML
        var corazonRojo: ImageView = findViewById<ImageView>(R.id.imageViewCorazon)
        var corazonNegro: ImageView = findViewById<ImageView>(R.id.imageViewCorazonNegro)
        val latidoAnimation =
            AnimatorInflater.loadAnimator(this, R.animator.latido_animation) as AnimatorSet

        // Establecer el objetivo de la animación (ImageView)
        latidoAnimation.setTarget(corazonRojo)

        // Iniciar la animación
        latidoAnimation.start()

        val latidoAnimation2 =
            AnimatorInflater.loadAnimator(this, R.animator.latido_animation_negro) as AnimatorSet

        // Establecer el objetivo de la animación (ImageView)
        latidoAnimation2.setTarget(corazonNegro)

        // Iniciar la animación
        latidoAnimation2.start()
    }
}