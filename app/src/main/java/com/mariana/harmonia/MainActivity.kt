package com.mariana.harmonia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.activitys.EligeModoJuegoActivity
import com.mariana.harmonia.activitys.InicioSesion
import com.mariana.harmonia.activitys.iniciarSesion.RegistroActivity
import com.mariana.harmonia.activitys.iniciarSesion.RestableceContrasenaActivity
import com.mariana.harmonia.activitys.Utilidades
import com.mariana.harmonia.activitys.Utilidades.Companion.colorearTexto
import com.mariana.harmonia.interfaces.PlantillaActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity(),PlantillaActivity {

    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 9001
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        colorearTexto(this, R.id.titleTextView)
        colorearTexto(this, R.id.registrateTextView)
        colorearTexto(this, R.id.recuerdasContrasena)

        //Inicializar firebase
        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion(firebaseAuth)
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

    fun clickNoRecuerdasLaContraseña(view: View){
        val intent = Intent(this, RestableceContrasenaActivity::class.java)
        startActivity(intent)
    }

    fun irRegistrate(view: View?){
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    fun irIniciarSesion(view: View) {

        val Email: TextView = findViewById(R.id.editText1)
        val contrasena: TextView = findViewById(R.id.editText2)

        // Validación de campos
        val emailText = Email.text.toString()
        val contrasenaText = contrasena.text.toString()

        if (emailText.isEmpty() || contrasenaText.isEmpty()) {
            Toast.makeText(baseContext, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación del formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(baseContext, "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de la longitud de la contraseña
        if (contrasenaText.length < 6) {
            Toast.makeText(baseContext, "La contraseña debe tener al menos 6 caracteres,asegurese de su contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(emailText, contrasenaText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, EligeModoJuegoActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    Log.e("InicioSesion", "Error al iniciar sesión", task.exception)
                    Toast.makeText(baseContext, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
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
            Toast.makeText(this, "Error durante el inicio de sesión con Google", Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificar si el código de solicitud coincide con el código de inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            // Obtener la cuenta de Google desde el resultado de la tarea
            val account = task.getResult(ApiException::class.java)
            // Autenticar con Firebase usando la cuenta de Google
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // Manejar el fallo en el inicio de sesión con Google
            Log.w(TAG, "Fallo en el inicio de sesión con Google. Código de error: ${e.statusCode}", e)
            Toast.makeText(
                this,
                "Fallo en el inicio de sesión con Google. Código de error: ${e.statusCode}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        try {
            // Obtener credenciales de autenticación de Google
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            // Autenticar con Firebase usando las credenciales de Google
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Autenticación exitosa, redirigir a la siguiente actividad
                        Log.d(TAG, "Inicio de sesión con credenciales de Google exitoso")

                        // Obtener información del usuario de la cuenta de Google
                        val userName = account?.displayName ?: "Usuario_${Random.nextInt(1000)}"

                        // Crear un nuevo usuario en Firebase con la información de la cuenta de Google
                        val newUser = firebaseAuth.currentUser
                        if (newUser != null) {
                            // Guardar el nombre de usuario en Firestore
                            val db = FirebaseFirestore.getInstance()
                            val userDoc = db.collection("users").document(newUser.uid)
                            userDoc.set(mapOf("username" to userName))
                                .addOnSuccessListener {
                                    // Continuar con la lógica de la aplicación, por ejemplo, redirigir a la siguiente actividad
                                    val intent = Intent(this, EligeModoJuegoActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    // Manejar el fallo al guardar el nombre de usuario
                                    Toast.makeText(this, "Error al guardar el nombre de usuario", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Manejar el fallo en la autenticación con Firebase
                            Log.w(TAG, "Fallo en la autenticación con Firebase", task.exception)
                            Toast.makeText(this, "Autenticación fallida.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } catch (e: Exception) {
            // Manejar errores durante la autenticación con Google
            Log.e(TAG, "Error durante la autenticación con Google", e)
            e.printStackTrace()
            Toast.makeText(this, "Error durante la autenticación con Google", Toast.LENGTH_SHORT).show()
        }
    }

    fun clickFireBase(view: View) {
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
}
