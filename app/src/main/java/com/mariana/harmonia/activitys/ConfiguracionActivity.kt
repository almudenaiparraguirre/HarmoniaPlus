package com.mariana.harmonia.activitys

import UserDao
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.auth.User
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.Utils

/**
 * Actividad que maneja la configuración del perfil del usuario.
 * Permite cambiar la contraseña, activar o desactivar la música, y realizar otras configuraciones.
 */
class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var contrasenaAnterior: EditText
    private lateinit var contrasenaNueva: EditText
    private lateinit var buttonCambiarContra: Button
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchMusica: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchOtraOpcion: Switch
    private lateinit var textViewEliminarCuenta: TextView
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchSonidos: Switch
    private var sonidosActivados: Boolean = false
    val auth: FirebaseAuth = FirebaseDB.getInstanceFirebase()
    private var isMusicPlaying: Boolean = false

    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val MUSIC_SWITCH_STATE = "musicSwitchState"
    }

    /**
     * Método llamado al crear la actividad. Inicializa las vistas y configura los listeners.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracion_activity)
        contrasenaAnterior = findViewById(R.id.edit_text_vieja_contrasena)
        contrasenaNueva = findViewById(R.id.edit_text_nueva_contrasena)
        switchMusica = findViewById(R.id.switchMusica)
        switchOtraOpcion = findViewById(R.id.switchSonidos)
        buttonCambiarContra = findViewById(R.id.buttonCambiarContrasena)
        textViewEliminarCuenta = findViewById(R.id.textViewEliminarCuenta)
        switchSonidos = findViewById(R.id.switchSonidos)
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        val switchSonidosActivado = sharedPreferences.getBoolean("sonidosSwitchState", false)
        switchSonidos.isChecked = switchSonidosActivado

        configurarSwitchColor(switchMusica)
        configurarSwitchColor(switchOtraOpcion)

        Utils.degradadoTexto(this, R.id.configuracionTextView, R.color.rosa, R.color.morado)
        Utils.degradadoTexto(this, R.id.contrasenaTituloTextView, R.color.rosa, R.color.morado)
        Utils.degradadoTexto(this, R.id.efectosYMusica, R.color.rosa, R.color.morado)
        Utils.degradadoTexto(this, R.id.textViewEliminarCuenta, R.color.rosa, R.color.morado)

        switchSonidos.setOnCheckedChangeListener { _, isChecked ->
            sonidosActivados = isChecked
            guardarEstadoSonidosSwitch(sonidosActivados)
            if (isChecked) {
                val thumbColor = ContextCompat.getColor(this, R.color.rosa)
                val trackColor = ContextCompat.getColor(this, R.color.rosa)
                mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
                mediaPlayer.start()
                sonidosActivados = true

                switchSonidos.thumbTintList = ColorStateList.valueOf(thumbColor)
                switchSonidos.trackTintList = ColorStateList.valueOf(trackColor)
            } else {
                val thumbColor = ContextCompat.getColor(this, R.color.gris)
                val trackColor = ContextCompat.getColor(this, R.color.grisClaro)

                mediaPlayer.release()
                sonidosActivados = false

                switchSonidos.thumbTintList = ColorStateList.valueOf(thumbColor)
                switchSonidos.trackTintList = ColorStateList.valueOf(trackColor)
            }
        }

        buttonCambiarContra.setOnClickListener {
            mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
            mediaPlayer.start()
            vibrarDispositivo()

            val contrasenaAnteriorText = contrasenaAnterior.text.toString()
            val contrasenaNuevaText = contrasenaNueva.text.toString()

            val user = FirebaseDB.getInstanceFirebase().currentUser

            // Verificar que la contraseña antigua es correcta
            val credential = EmailAuthProvider.getCredential(user!!.email!!, contrasenaAnteriorText)
            user.reauthenticate(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        // La autenticación es exitosa, ahora verifica las reglas para la nueva contraseña
                        if (contrasenaAnteriorText == contrasenaNuevaText) {
                            Toast.makeText(this, "Las contraseñas introducidas son iguales", Toast.LENGTH_SHORT).show()
                        } else if (contrasenaNuevaText.length < 8 || !contrasenaNuevaText.any { it.isDigit() } ||
                            !contrasenaNuevaText.any { it.isUpperCase() } || !contrasenaNuevaText.any { it.isLowerCase() }
                        ) {
                            Toast.makeText(
                                this,
                                "La nueva contraseña debe tener al menos 8 caracteres, incluir al menos 1 mayúscula, 1 minúscula y 1 número",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            // Aplicar la actualización de contraseña si cumple con todas las reglas
                            user.updatePassword(contrasenaNuevaText)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Log.d(TAG, "User password updated.")
                                        Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e(TAG, "Error updating password", updateTask.exception)
                                        Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        // La autenticación falló, muestra un mensaje de error
                        Toast.makeText(this, "Error de autenticación: La contraseña antigua es incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        textViewEliminarCuenta.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        switchMusica.setOnCheckedChangeListener { _, isChecked ->
            guardarEstadoSwitch(MUSIC_SWITCH_STATE, isChecked)

            if (isChecked) {
                val thumbColor = ContextCompat.getColor(this, R.color.rosa)
                val trackColor = ContextCompat.getColor(this, R.color.rosa)

                mediaPlayer = MediaPlayer.create(this, R.raw.cancion_fondo)
                mediaPlayer.start()

                switchMusica.thumbTintList = ColorStateList.valueOf(thumbColor)
                switchMusica.trackTintList = ColorStateList.valueOf(trackColor)
            } else {
                val thumbColor = ContextCompat.getColor(this, R.color.gris)
                val trackColor = ContextCompat.getColor(this, R.color.grisClaro)

                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                }

                switchMusica.thumbTintList = ColorStateList.valueOf(thumbColor)
                switchMusica.trackTintList = ColorStateList.valueOf(trackColor)
            }
        }
    }

    /**
     * Detiene la reproducción de la música al poner la actividad en segundo plano.
     */
    override fun onStop() {
        super.onStop()
        detenerReproduccionMusica()
    }

    /**
     * Configura la reproducción de la música al reanudar la actividad.
     */
    override fun onResume() {
        super.onResume()

        if (switchMusica.isChecked) {
            configurarReproduccionMusica()
        }
    }

    /**
     * Configura la reproducción de la música y actualiza los colores de los switches.
     */
    private fun configurarReproduccionMusica() {
        if (!isMusicPlaying) {
            val thumbColor = ContextCompat.getColor(this, R.color.rosa)
            val trackColor = ContextCompat.getColor(this, R.color.rosa)

            mediaPlayer = MediaPlayer.create(this, R.raw.cancion_fondo)
            mediaPlayer.start()

            switchMusica.thumbTintList = ColorStateList.valueOf(thumbColor)
            switchMusica.trackTintList = ColorStateList.valueOf(trackColor)
            isMusicPlaying = true
        }
    }

    /**
     * Detiene la reproducción de la música y guarda el estado del switch en las preferencias compartidas.
     */
    private fun detenerReproduccionMusica() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            isMusicPlaying = false
        }
    }

    /**
     * Pausa la música y guarda el estado del switch al poner la actividad en segundo plano.
     */
    override fun onPause() {
        super.onPause()
        detenerReproduccionMusica()
        guardarEstadoSwitch(MUSIC_SWITCH_STATE, switchMusica.isChecked)
    }

    /**
     * Navega a la actividad del perfil del usuario.
     * @param view
     */
    fun irPerfilUsuario(view: View){
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in_config_perfil, R.anim.fade_out);
    }

    /**
     * Vibra el dispositivo cuando se hace clic en un botón.
     */
    private fun vibrarDispositivo() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    /**
     * Regresa a la actividad anterior.
     */
    fun volverModoJuego(view: View){
        val user = FirebaseDB.getInstanceFirebase().currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            println("$name - $email")
        }
        mediaPlayer.start()
        onBackPressed()
    }

    /**
     * Actualiza la contraseña del usuario.
     */
    fun actualizarContrasena(){
        mediaPlayer.start()
        if (contrasenaAnterior == contrasenaNueva){
            Toast.makeText(this, "Las contraseñas introducidas son iguales", Toast.LENGTH_SHORT).show()
        }
        else{
            val user = FirebaseDB.getInstanceFirebase().currentUser
            val newPassword = contrasenaNueva.toString()

            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User password updated.")
                        Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                    }
                }
            Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Guarda el estado del switch en las preferencias compartidas.
     */
    private fun guardarEstadoSwitch(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * Guarda el estado del switch de sonidos en las preferencias compartidas.
     */
    private fun guardarEstadoSonidosSwitch(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("sonidosSwitchState", value)
        editor.apply()
    }

    /**
     * Configura los colores del switch.
     */
    private fun configurarSwitchColor(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            val thumbColor = if (isChecked) {
                ContextCompat.getColor(this, R.color.rosa)
            } else {
                ContextCompat.getColor(this, R.color.gris)
            }

            val trackColor = if (isChecked) {
                ContextCompat.getColor(this, R.color.rosa)
            } else {
                ContextCompat.getColor(this, R.color.grisClaro)
            }

            switch.thumbTintList = ColorStateList.valueOf(thumbColor)
            switch.trackTintList = ColorStateList.valueOf(trackColor)
        }
    }

    /**
     * Cierra la sesión del usuario y navega a la actividad principal.
     */
    fun cerrarSesionConfig(view: View){
        mediaPlayer.start()
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Elimina la cuenta del usuario y navega a la actividad principal.
     */
    private fun eliminarMiCuenta() {
        mediaPlayer.start()
        val user = FirebaseDB.getInstanceFirebase().currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
        auth.signOut()
        Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Muestra un cuadro de diálogo de confirmación antes de eliminar la cuenta del usuario.
     */
    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Cuenta")
        builder.setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer y perderás todos los progresos")
        builder.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            mediaPlayer.start()
            eliminarMiCuenta()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            mediaPlayer.start()
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}