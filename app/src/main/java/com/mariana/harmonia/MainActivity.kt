package com.mariana.harmonia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.activitys.EligeModoJuegoActivity
import com.mariana.harmonia.activitys.InicioSesion
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity
import com.mariana.harmonia.activitys.iniciarSesion.RestableceContrasenaActivity
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.activitys.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.PlantillaActivity
import android.Manifest
import android.media.MediaPlayer
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.mariana.harmonia.models.entity.User
import com.mariana.harmonia.utils.HashUtils
import java.time.LocalDate

class MainActivity : AppCompatActivity(), PlantillaActivity {

    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 9001
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer

    private val NOTIFICATION_ID = 1
    val CHANNEL_ID = "mi_canal_de_notificacion"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        colorearTexto(this, R.id.titleTextView)
        colorearTexto(this, R.id.registrateTextView)
        colorearTexto(this, R.id.recuerdasContrasena)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

        //Inicializar firebase
        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion(firebaseAuth)

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
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null && this !is MainActivity) {
            // No hay usuario autenticado y no estamos en la pantalla de inicio de sesión,
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

        val Email: TextView = findViewById(R.id.editText1)
        val contrasena: TextView = findViewById(R.id.editText2)

        // Validación de campos
        val emailText = Email.text.toString()
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
        println(emailText + "/" + emailEncriptado)

        val usersRef = FirebaseFirestore.getInstance().collection("usuarios")

// Verificar si el correo electrónico está presente en la colección de usuarios
        usersRef.whereEqualTo("email", emailEncriptado).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    firebaseAuth.signInWithEmailAndPassword(emailText, contrasenaText)
                        .addOnCompleteListener(this) { task ->

                            Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, EligeModoJuegoActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    mediaPlayer.start()
                }else {  Toast.makeText(baseContext, " No se pudo iniciar sesión", Toast.LENGTH_SHORT)}
            }
    }

    fun irSalir(view: View) {
        Utilidades.salirAplicacion(this)
    }

    override fun onBackPressed() {
        Utilidades.salirAplicacion(this)
        super.onBackPressed()
    }

    fun iniciarSesionPruebas(view: View?) {
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
            .requestEmail()
            .build()

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
        try {
            // Obtener credenciales de autenticación de Google
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            // Autenticar con Firebase usando las credenciales de Google
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Obtener la cuenta de usuario de Firebase
                        val firebaseUser = firebaseAuth.currentUser
                        // Obtener el nombre y el correo electrónico del usuario de la cuenta de Google
                        val googleName = account?.displayName
                        val googleEmail = account?.email
                        Log.d(TAG, "Nombre de Google: $googleName")
                        Log.d(TAG, "Correo electrónico de Google: $googleEmail")
                        val fechaRegistro = LocalDate.now()

                        val user = User(
                            email = googleEmail?.lowercase(),
                            name = googleEmail,
                            correo = googleEmail,
                            355,
                            1,
                            mesRegistro = fechaRegistro.month,
                            anioRegistro = fechaRegistro.year
                        )
                        UserDao.addUser(user)

                        // Autenticación exitosa, redirigir a la siguiente actividad
                        Log.d(
                            TAG,
                            "Inicio de sesión con credenciales de Google exitoso"
                        )
                        val intent = Intent(this, EligeModoJuegoActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Manejar el fallo en la autenticación con Firebase
                        Log.w(
                            TAG,
                            "Fallo en la autenticación con Firebase",
                            task.exception
                        )
                        Toast.makeText(
                            this,
                            "Autenticación fallida.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: Exception) {
            // Manejar errores durante la autenticación con Google
            Log.e(TAG, "Error durante la autenticación con Google", e)
            e.printStackTrace()
            Toast.makeText(
                this,
                "Error durante la autenticación con Google",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

/* private fun showNotification() {
     // Crear y mostrar la notificación
     val textTitle = "¡Bienvenidooooooooooo!"
     val textContent = "Graciaaaaaaas por usar nuestra aplicación."

     val builder = NotificationCompat.Builder(this, CHANNEL_ID)
         .setSmallIcon(R.drawable.nota_3b)
         .setContentTitle(textTitle)
         .setContentText(textContent)
         .setPriority(NotificationCompat.PRIORITY_DEFAULT)

     // Obtener el NotificationManager
     val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

     // Crear el canal de notificación si es necesario (para versiones de Android >= 8.0)
     createNotificationChannel()

     // Mostrar la notificación
     notificationManager.notify(0, builder.build())

     // Mostrar la notificación con permisos
     with(NotificationManagerCompat.from(this)) {
         if (ActivityCompat.checkSelfPermission(
                 this@MainActivity,
                 Manifest.permission.POST_NOTIFICATIONS
             ) != PackageManager.PERMISSION_GRANTED
         ) {


             return@with
         }
         // notificationId is a unique int for each notification that you must define.
         notify(NOTIFICATION_ID, builder.build())


     }
*/


