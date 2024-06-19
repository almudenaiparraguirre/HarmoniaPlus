package com.mariana.harmonia.activities

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.messaging.FirebaseMessaging

import com.mariana.harmonia.MainActivity

import com.mariana.harmonia.R
import com.mariana.harmonia.activities.fragments.CargaFragment
import com.mariana.harmonia.activities.fragments.FragmentoDificultadDesafio
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.ServicioActualizacion
import com.mariana.harmonia.utils.Utils
import com.mariana.harmonia.utils.UtilsDB
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Actividad principal para elegir el modo de juego.
 */
class EligeModoJuegoActivity : AppCompatActivity(), PlantillaActivity {

    companion object{
        // Se usa para poder llamarla desde otras activitis
        lateinit var instance: EligeModoJuegoActivity
    }
    private   var RC_NOTIFICATION = 99
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var nombreTextView: TextView
    lateinit var porcentajeTextView: TextView
    private lateinit var imageViewFotoPerfil: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var botonAventura: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonDesafio: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonAjustes: androidx.appcompat.widget.AppCompatButton
    private lateinit var botonRanking: androidx.appcompat.widget.AppCompatButton

    /**
     * Función llamada al crear la actividad.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var firebaseMessagin:FirebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessagin.subscribeToTopic("new_user_forums")

        setContentView(R.layout.elige_modo_juego_activity) // Inflar el layout primero


        Utils.isExternalStorageWritable()
        Utils.isExternalStorageReadable()

        val imageView: ImageView = findViewById(R.id.fondoImageView)
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.animacion_principal)
        imageView.startAnimation(anim)
        instance = this
        nombreTextView = findViewById(R.id.nombreModoDeJuego)
        porcentajeTextView = findViewById(R.id.porcentajeTextView)
        progressBar = findViewById(R.id.progressBarCarga)
        imageViewFotoPerfil = findViewById(R.id.imageViewFotoPerfil)

        botonAventura = findViewById(R.id.botonAventura)
        botonDesafio = findViewById(R.id.botonDesafio)
        botonAjustes = findViewById(R.id.botonOpciones)
        botonRanking = findViewById(R.id.botonRanking)


        inicilalizarVariablesThis()
        inicializarConBase()
        ocultarFragmento()

        lifecycleScope.launch {
            downloadImage2()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), RC_NOTIFICATION)
        }
        val serviceIntent = Intent(this, ServicioActualizacion::class.java)
        startService(serviceIntent)
        println("SERVICIO CONTADOR COMENZO")
        crearFragmentoCarga()

        botonRanking.setOnClickListener {
         irRanking()

        }

        //animacion Botones


        //animacion Botones


        val botones = listOf(botonAventura, botonDesafio, botonAjustes)

        // Define la animación de escala para cada botón
        val animacionAgrandar = botones.map {
            val scaleX = ObjectAnimator.ofFloat(it, "scaleX", 1.2f)
            val scaleY = ObjectAnimator.ofFloat(it, "scaleY", 1.2f)
            AnimatorSet().apply {
                play(scaleX).with(scaleY)
                duration = 300 // Duración de la animación en milisegundos
            }
        }

        // Define la animación de escala para restaurar el tamaño original del botón
        val animacionReducir = botones.map {
            val scaleX = ObjectAnimator.ofFloat(it, "scaleX", 1.0f)
            val scaleY = ObjectAnimator.ofFloat(it, "scaleY", 1.0f)
            AnimatorSet().apply {
                play(scaleX).with(scaleY)
                duration = 300 // Duración de la animación en milisegundos
            }
        }

        botones.forEachIndexed { index, button ->
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Inicia la animación de agrandar cuando se toca el botón
                        animacionAgrandar[index].start()
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        // Detiene la animación de agrandar y inicia la animación de reducir cuando se suelta el botón
                        animacionAgrandar[index].cancel()
                        animacionReducir[index].start()

                        // Ejecuta la acción correspondiente al botón
                        when (button.id) {
                            R.id.botonAventura -> irModoAventura()
                            R.id.botonDesafio -> irDesafio()
                            R.id.botonOpciones -> clickOpciones()
                        }

                        true
                    }

                    else -> false
                }
            }
        }
    }





    private fun incializarConexionNotificaciones() {
        var firebaseMessagin:FirebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessagin.subscribeToTopic("new_user_forums")
    }

    /**
     * Función para crear y mostrar el fragmento de carga.
     */
    fun crearFragmentoCarga(){
        val fragment = CargaFragment()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container_carga, fragment)

        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
        ocultarFragmento()
    }

    /**
     * Función para inicializar datos de la interfaz basándose en la base de datos.
     */
    fun inicializarConBase() = runBlocking {
        val experiencia = UtilsDB.getExperiencia()

        val nivel = calcularNivel(experiencia!!)
        var experienciaSobrante = UtilsDB.getExperiencia()!!%100

        nombreTextView.text = UtilsDB.getNombre()
        porcentajeTextView.text = "NV. "+nivel.toString()
        progressBar.progress = experienciaSobrante
    }

    /**
     * Función para inicializar variables específicas de esta actividad.
     */
    private fun inicilalizarVariablesThis() {
        Utils.degradadoTexto(this, R.id.cerrarSesion,R.color.rosa,R.color.morado)
        Utils.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)
        Utils.degradadoTexto(this, R.id.eligeModo,R.color.rosa,R.color.morado)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        //Utils.serializeImage(this,R.mipmap.img_gema)
        //imageViewFotoPerfil.setImageBitmap(Utils.deserializeImage(this,"/storage/emulated/0/Download/imagenSerializada.json"))
    }

    /**
     * Función para descargar la imagen de perfil del usuario desde Firebase Storage.
     */
    private fun downloadImage2() {
        val storageRef = FirebaseDB.getInstanceStorage().reference
        val userId = FirebaseDB.getInstanceFirebase().currentUser?.uid
        val imagesRef = storageRef.child("imagenesPerfilGente").child("$userId.jpg")

        imagesRef.downloadUrl.addOnSuccessListener { url ->
            Glide.with(this)
                .load(url)
                .into(imageViewFotoPerfil)

            // Call the function to change the name and upload the image
            /*runBlocking {
                changeAndUploadImage(url)
            }*/
        }.addOnFailureListener { exception ->
            println("Error al cargar la imagen: ${exception.message}")
            imageViewFotoPerfil.setImageResource(R.mipmap.fotoperfil_guitarra)
        }
    }
    fun calcularNivel(experiencia: Int): Int {
        val N_max = 100  // Nivel máximo deseado
        val c = 3     // Factor de ajuste
        var nivel = experiencia.toDouble() / (c * N_max)
        // Asegurarse de que el nivel no supere el nivel máximo deseado
        nivel = nivel.coerceAtMost(N_max.toDouble())
        // Redondear el nivel a un número entero si es necesario
        nivel = nivel
        return nivel.toInt()
    }

    /**
     * Función para abrir la actividad del perfil del usuario.
     */
    fun menu_perfil(view: View){
        mediaPlayer.start()
        mostrarFragmento()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        inicializarConBase()
    }

    /**
     * Función para cerrar la sesión del usuario.
     */
    fun cerrarSesion(view: View) {
        mediaPlayer.start()
        val serviceIntent = Intent(this, ServicioActualizacion::class.java)
        stopService(serviceIntent)
        println("SERVICIO CONTADOR CERRADO")
        FirebaseDB.getInstanceFirebase().signOut()
        UtilsDB.currentUser?.reload()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        finishAffinity()
    }

    /**
     * Función para abrir la actividad de configuración.
     */
    fun clickOpciones() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(findViewById(R.id.botonOpciones))
        mediaPlayer.start()
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
    }


    /**
     * Función para abrir la actividad de niveles de aventura.
     */
    fun irModoAventura() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(findViewById(R.id.botonAventura))
        mostrarFragmento()
        mediaPlayer.start()
        val intent = Intent(this, NivelesAventuraActivity::class.java)
        startActivity(intent)
        mostrarFragmento()
    }


    /**
     * Función para abrir la actividad de desafío.
     */
    fun irDesafio() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(findViewById(R.id.botonDesafio))
        mediaPlayer.start()

        // Crea una instancia del fragmento que deseas mostrar
        val fragment = FragmentoDificultadDesafio()

        // Obtén el administrador de fragmentos y comienza una transacción
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Reemplaza el contenido del contenedor de fragmentos con el fragmento que deseas mostrar
        fragmentTransaction.replace(R.id.fragment_container_desafio, fragment)

        // Agrega la transacción al historial de retroceso (opcional)
        fragmentTransaction.addToBackStack(null)

        // Realiza la transacción
        fragmentTransaction.commit()
    }
    fun irRanking() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(findViewById(R.id.botonRanking))
        mostrarFragmento()
        mediaPlayer.start()
        val intent = Intent(this, RankingActivity::class.java)
        startActivity(intent)


    }

    /**
     * Función para manejar el botón de retroceso.
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * Función para ocultar el fragmento de carga.
     */
    fun ocultarFragmento(){
        var fragmento =  findViewById<FrameLayout>(R.id.fragment_container_carga)
        fragmento.visibility = View.GONE
    }

    /**
     * Función para mostrar el fragmento de carga.
     */
    fun mostrarFragmento(){
        var fragmento =  findViewById<FrameLayout>(R.id.fragment_container_carga)
        fragmento.visibility = View.VISIBLE
    }

    /**
     * Función para manejar los resultados de la solicitud de permisos.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_NOTIFICATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ALLOWED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        ocultarFragmento()
    }
}