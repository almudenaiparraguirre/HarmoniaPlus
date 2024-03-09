package com.mariana.harmonia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
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
import android.media.MediaPlayer
import androidx.annotation.RequiresApi
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.models.entity.User
import com.mariana.harmonia.utils.HashUtils
import com.mariana.harmonia.utils.Utils
import java.time.LocalDate

class MainActivity : AppCompatActivity(), PlantillaActivity {

    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 9001

    private lateinit var mediaPlayer: MediaPlayer

    val CHANNEL_ID = "mi_canal_de_notificacion"


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
    }

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

    fun clickNoRecuerdasLaContraseña(view: View) {
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
        mediaPlayer.start()
    }

    fun irRegistrate(view: View?) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
        mediaPlayer.start()
    }

    fun irIniciarSesion(view: View) {

        mediaPlayer.start()
        val Email: TextView = findViewById(R.id.editText1)
        val contrasena: TextView = findViewById(R.id.editText2)

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
                    FirebaseDB.getInstanceFirebase().signInWithEmailAndPassword(emailText, contrasenaText)
                        .addOnSuccessListener {
                            // Autenticación exitosa
                            Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, EligeModoJuegoActivity::class.java)
                            startActivity(intent)
                            finish()
                            mediaPlayer.start()
                        }
                        .addOnFailureListener { exception ->
                            // Manejar el fallo en el inicio de sesión con Firebase
                            Toast.makeText(baseContext, "Error al iniciar sesión: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // El correo electrónico no está presente en la colección de usuarios
                    Toast.makeText(baseContext, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el fallo en la consulta a la colección de usuarios
                Toast.makeText(baseContext, "Error al verificar el correo electrónico: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }

    fun irSalir(view: View) {
        Utils.salirAplicacion(this)
    }

    override fun onBackPressed() {
        Utils.salirAplicacion(this)
        super.onBackPressed()
    }

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

    private fun signInWithGoogle() {
        // Configuración de inicio de sesión con Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1087712210246-pf3t0kpf6jo2fgjq8me6cmidn7s7f348.apps.googleusercontent.com")
            .requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificar si el código de solicitud coincide con el código de inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

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
}