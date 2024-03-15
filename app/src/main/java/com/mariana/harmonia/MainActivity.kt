package com.mariana.harmonia

import UserDao
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mariana.harmonia.activitys.EligeModoJuegoActivity
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity
import com.mariana.harmonia.activitys.iniciarSesion.RestableceContrasenaActivity
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.models.entity.User
import com.mariana.harmonia.utils.HashUtils
import com.mariana.harmonia.utils.Utils
import java.time.LocalDate

/**
 * Actividad principal que representa la pantalla de inicio de la aplicación.
 */
class MainActivity : AppCompatActivity(), PlantillaActivity {

    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 9001

    private lateinit var mediaPlayer: MediaPlayer

    val CHANNEL_ID = "mi_canal_de_notificacion"

    /**
     * Se llama cuando se crea la actividad.
     *
     * @param savedInstanceState Estado de la instancia guardada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Utils.degradadoTexto(this, R.id.titleTextView, R.color.rosa, R.color.morado)
        Utils.degradadoTexto(this, R.id.registrateTextView, R.color.rosa, R.color.morado)
        Utils.degradadoTexto(this, R.id.recuerdasContrasena, R.color.rosa, R.color.morado)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

        //Inicializar firebase

        comprobarSesion(FirebaseDB.getInstanceFirebase())

        // Crear y mostrar la notificación
        val textTitle = "¡Bienvenidoooooooooooo!"
        val textContent = "Graciaaaaaaas por usar nuestra aplicación."

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.nota_3b)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Welcome Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Mostrar la notificación
        notificationManager.notify(0, builder.build())
        animacionInicio()

        val interpolator = AccelerateDecelerateInterpolator()

        val kbv = findViewById<KenBurnsView>(R.id.fondoImageView) as KenBurnsView
        val generator = RandomTransitionGenerator(9000L, interpolator)
        kbv.setTransitionGenerator(generator)
        kbv.restart()
    }

    /**
     * Función que comprueba el estado de la sesión.
     *
     * @param firebaseAuth Instancia de FirebaseAuth.
     */
    fun comprobarSesion(firebaseAuth: FirebaseAuth) {
        val firebaseUser = FirebaseDB.getInstanceFirebase().currentUser

        if (firebaseUser == null && this !is MainActivity) {

            // redirigir a la pantalla de inicio de sesión
        } else if (firebaseUser != null) {
            // Hay un usuario autenticado, redirigir a la pantalla principal
            val intent = Intent(this, EligeModoJuegoActivity::class.java)
            startActivity(intent)
            finishAffinity() // Cierra todas las actividades anteriores
        }
    }

    /**
     * Función que realiza la animación de inicio de la pantalla principal.
     */
    fun animacionInicio() {
        // Obtén las referencias a tus elementos
        val tituloLogo = findViewById<LinearLayout>(R.id.TituloLogo)
        val bienvenido = findViewById<LinearLayout>(R.id.bienvenido)
        val introduce = findViewById<TextView>(R.id.introduce)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextContraseña = findViewById<EditText>(R.id.editTextContraseña)
        val recuerdasContrasena = findViewById<TextView>(R.id.recuerdasContrasena)
        val signGoogle = findViewById<CardView>(R.id.signGoogle)
        val botonIniciarSesion = findViewById<AppCompatButton>(R.id.botonIniciarSesion)
        val registrate = findViewById<LinearLayout>(R.id.registrate)
        val salirTextView = findViewById<TextView>(R.id.salirTextView)

        // Verificar si alguna de las vistas es nula
        if (tituloLogo == null || bienvenido == null || introduce == null ||
            editTextEmail == null || editTextContraseña == null ||
            recuerdasContrasena == null || signGoogle == null ||
            botonIniciarSesion == null || registrate == null || salirTextView == null) {
            Log.e(TAG, "Una o más vistas no se han inicializado correctamente")
            return
        }

        // Aplicar animaciones a las vistas
        YoYo.with(Techniques.FadeInUp).duration(3000).playOn(tituloLogo)
        YoYo.with(Techniques.FadeInUp).duration(3000).playOn(bienvenido)
        YoYo.with(Techniques.FadeInLeft).duration(3000).playOn(editTextEmail)
        YoYo.with(Techniques.FadeInRight).duration(3000).playOn(editTextContraseña)
        YoYo.with(Techniques.FadeInDown).duration(3000).playOn(signGoogle)
        YoYo.with(Techniques.FadeInDown).duration(3000).playOn(botonIniciarSesion)

        YoYo.with(Techniques.FadeOut).duration(1).playOn(introduce)
        YoYo.with(Techniques.FadeOut).duration(1).playOn(recuerdasContrasena)
        YoYo.with(Techniques.FadeOut).duration(1).playOn(registrate)
        YoYo.with(Techniques.FadeOut).duration(1).playOn(salirTextView)

        YoYo.with(Techniques.FadeInLeft).delay(3000).duration(5000).playOn(introduce)
        YoYo.with(Techniques.FadeInLeft).delay(3000).duration(5000).playOn(recuerdasContrasena)
        YoYo.with(Techniques.FadeInLeft).delay(3000).duration(5000).playOn(registrate)
        YoYo.with(Techniques.FadeInLeft).delay(3000).duration(5000).playOn(salirTextView)

        registrate.visibility = View.VISIBLE
        salirTextView.visibility = View.VISIBLE
    }


    /**
     * Función que se llama al hacer clic en el botón "No recuerdas la contraseña".
     *
     * @param view Vista del botón.
     */
    fun clickNoRecuerdasLaContraseña(view: View) {
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
        mediaPlayer.start()
    }

    /**
     * Función que redirige a la pantalla de registro al hacer clic en el botón "Regístrate".
     *
     * @param view Vista del botón.
     */
    fun irRegistrate(view: View?) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
        mediaPlayer.start()
    }

    /**
     * Función que inicia sesión con el correo electrónico y la contraseña proporcionados.
     *
     * @param view Vista del botón.
     */
    fun irIniciarSesion(view: View) {

        mediaPlayer.start()
        val Email: TextView = findViewById(R.id.editTextEmail)
        val contrasena: TextView = findViewById(R.id.editTextContrasena)

        // Validación de campos
        val emailText = Email.text.toString().lowercase()
        val contrasenaText = contrasena.text.toString()

        if (emailText.isEmpty() || contrasenaText.isEmpty()) {
            Toast.makeText(baseContext, "Por favor, completa todos los campos", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Validación del formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(
                baseContext,
                "Formato de correo electrónico no válido",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validación de la longitud de la contraseña
        if (contrasenaText.length < 6) {
            Toast.makeText(
                baseContext,
                "La contraseña debe tener al menos 6 caracteres,asegurese de su contraseña",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val emailEncriptado = HashUtils.sha256(emailText)
        println("$emailText/$emailEncriptado")

        val usersRef = FirebaseDB.getInstanceFirestore().collection("usuarios")

// Verificar si el correo electrónico está presente en la colección de usuarios
        // Verificar si el correo electrónico está presente en la colección de usuarios
        usersRef.whereEqualTo("email", emailEncriptado).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // El correo electrónico está presente, intentar iniciar sesión
                    FirebaseDB.getInstanceFirebase()
                        .signInWithEmailAndPassword(emailText, contrasenaText)
                        .addOnSuccessListener {
                            // Autenticación exitosa
                            Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, EligeModoJuegoActivity::class.java)
                            startActivity(intent)
                            finish()
                            mediaPlayer.start()
                        }
                        .addOnFailureListener { exception ->
                            // Manejar el fallo en el inicio de sesión con Firebase
                            Toast.makeText(
                                baseContext,
                                "Error al iniciar sesión: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // El correo electrónico no está presente en la colección de usuarios
                    Toast.makeText(
                        baseContext,
                        "Email o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el fallo en la consulta a la colección de usuarios
                Toast.makeText(
                    baseContext,
                    "Error al verificar el correo electrónico: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    /**
     * Función que sale de la aplicación
     *
     * @param view Vista del botón.
     */
    fun irSalir(view: View) {
        Utils.salirAplicacion(this)
    }

    /**
     * Función que retrocede a la ventana anterior
     */
    override fun onBackPressed() {
        Utils.salirAplicacion(this)
        super.onBackPressed()
    }

    /**
     * Función que realiza el proceso de inicio de sesión con Google.
     *
     * @param view Vista del botón.
     */
    fun iniciarSesionGoogle(view: View?) {
        try {
            signInWithGoogle()
        } catch (e: Exception) {
            Log.e(TAG, "Error durante el inicio de sesión con Google", e)
            Toast.makeText(
                this,
                "Error durante el inicio de sesión con Google",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    /**
     * Función que inicia el proceso de inicio de sesión con Google.
     */
    private fun signInWithGoogle() {
        // Configuración de inicio de sesión con Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1087712210246-pf3t0kpf6jo2fgjq8me6cmidn7s7f348.apps.googleusercontent.com")
            .requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Sobrescribe el método onActivityResult para manejar el resultado de la actividad de inicio de sesión con Google.
     *
     * @param requestCode Código de solicitud pasado a startActivityForResult.
     * @param resultCode  Resultado de la actividad que indica si la operación fue exitosa.
     * @param data        Datos asociados con el resultado de la actividad.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificar si el código de solicitud coincide con el código de inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    /**
     * Maneja el resultado de la tarea de inicio de sesión con Google.
     *
     * @param task Tarea que contiene el resultado de la autenticación con Google.
     * Se intenta obtener la cuenta de Google desde el resultado de la tarea y autenticar
     * con Firebase utilizando las credenciales de Google. En caso de un error (por ejemplo,
     * ApiException), se registra un mensaje de error y se muestra un Toast informando
     * al usuario sobre el fallo en el inicio de sesión con Google.
     *
     * @see ApiException
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            // Obtener la cuenta de Google desde el resultado de la tarea
            val account = task.getResult(ApiException::class.java)
            // Autenticar con Firebase usando la cuenta de Google
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // Manejar el fallo en el inicio de sesión con Google
            Log.w(TAG, "Fallo en el inicio de sesión con Google", e)
            Toast.makeText(
                this,
                "Fallo en el inicio de sesión con Google",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Autentica al usuario con Firebase utilizando las credenciales de Google.
     *
     * @param account Cuenta de Google utilizada para la autenticación.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        // Obtener credenciales de autenticación de Google
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        // Autenticar con Firebase usando las credenciales de Google
        FirebaseDB.getInstanceFirebase().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Obtener la cuenta de usuario de Firebase
                    val googleName = account?.displayName
                    val googleEmail = account?.email
                    val emailEncriptado = googleEmail?.let { HashUtils.sha256(it.lowercase()) }

                    val usersRef = FirebaseDB.getInstanceFirestore().collection("usuarios")

                    // Verificar si el correo electrónico está presente en la colección de usuarios
                    usersRef.whereEqualTo("email", emailEncriptado).get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {

                                val intent = Intent(this, EligeModoJuegoActivity::class.java)
                                startActivity(intent)
                                finish()
                                mediaPlayer.start()
                            } else {

                                Log.d(TAG, "Nombre de Google: $googleName")
                                Log.d(TAG, "Correo electrónico de Google: $googleEmail")
                                val fechaRegistro = LocalDate.now()


                                val user = User(
                                    email = emailEncriptado,
                                    name = googleName,
                                    correo = googleEmail?.lowercase(),
                                    355,
                                    1,
                                    mesRegistro = fechaRegistro.month,
                                    anioRegistro = fechaRegistro.year
                                )

                                UserDao.addUser(user)
                                val intent = Intent(this, EligeModoJuegoActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                }
            }
    }
    object Registro {
        private val usuariosExistentes = listOf("Alberto","David")
        /**

        El input no es válido si...
        ...el username está vacío
        ...el username existe previamente
        ...la confirmedPassword no es igual que la password
        ...la password es menor de 2 dígitos
         */
        fun validarInputRegistro(
            username: String,
            password: String,
            confirmedPassword: String):Boolean{
            if (username.isEmpty() || password.isEmpty()){
                return false}
            if(username in usuariosExistentes){
                return false}
            if(password != confirmedPassword){
                return false}
            if(password.count {it.isDigit()} < 2){
                return false}
            return true}

        fun validarContraseña(contraseña: String): Boolean {
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
            return regex.matches(contraseña)
        }
    }
}