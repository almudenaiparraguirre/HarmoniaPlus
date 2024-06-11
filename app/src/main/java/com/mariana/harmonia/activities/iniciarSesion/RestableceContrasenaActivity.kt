package com.mariana.harmonia.activities.iniciarSesion

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.Utils

/**
 * RestableceContrasenaActivity es una actividad que permite a los usuarios restablecer su contraseña.
 * Se utiliza Firebase para enviar un correo electrónico de restablecimiento de contraseña.
 */
class RestableceContrasenaActivity : AppCompatActivity(), PlantillaActivity {

    // Declaración de variables
    private lateinit var boton: Button
    private lateinit var email: EditText
    private lateinit var textoAdvertencia: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer

    /**
     * Método llamado cuando la actividad se está creando. Se encarga de inicializar la interfaz de usuario
     * y otros componentes necesarios.
     * @param savedInstanceState Si no es nulo, esta actividad está siendo reconstituida a partir de un estado guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restablece_contrasena_activity)

        boton = findViewById(R.id.botonEmail)
        email = findViewById(R.id.editTextEmail)
        textoAdvertencia = findViewById(R.id.textoAdvertencia)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

        Utils.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)

        // Inicializa FirebaseAuth
        auth = FirebaseDB.getInstanceFirebase()

        boton.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (emailText.isEmpty()) {
                textoAdvertencia.visibility = View.VISIBLE
                Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
            } else if (!isValidEmail(emailText)) {
                textoAdvertencia.visibility = View.VISIBLE
                Log.d("RestableceContrasena", "Formato de correo electrónico no válido")
            } else {
                textoAdvertencia.visibility = View.GONE

                // Asegúrate de haber inicializado FirebaseAuth antes de usarlo
                auth.sendPasswordResetEmail(emailText).addOnSuccessListener {
                    Toast.makeText(this, "Por favor revisar el email", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Método llamado al hacer clic en el botón para enviar el correo electrónico.
     */
    fun enviarEmail(view: View) {
        val emailText = email.text.toString().trim()

        if (emailText.isEmpty()) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "No se puede enviar el correo porque el email está vacío")
        } else if (!isValidEmail(emailText)) {
            textoAdvertencia.visibility = View.VISIBLE
            Log.d("RestableceContrasena", "Formato de correo electrónico no válido")
        } else {
            textoAdvertencia.visibility = View.GONE
            val intent = Intent(this, EnvioCodigoActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Método para comprobar que el email introducido es válido.
     * @param email Valida el email
     * @return true si el email es válido, false de lo contrario.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    /**
     * Método llamado al hacer clic en el botón para volver al inicio de sesión.
     * @param view Es la vista
     */
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}