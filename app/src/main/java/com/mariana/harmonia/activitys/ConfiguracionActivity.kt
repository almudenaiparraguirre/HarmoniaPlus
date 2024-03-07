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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.auth.User
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.databinding.InicioSesionActivityBinding
import com.mariana.harmonia.models.db.FirebaseDB
import com.mariana.harmonia.utils.Utils

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

    companion object {
        const val SHARED_PREFS = "sharedPrefs"
        const val MUSIC_SWITCH_STATE = "musicSwitchState"
    }

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
        }

        textViewEliminarCuenta.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        switchMusica.setOnCheckedChangeListener { _, isChecked ->
            guardarEstadoSwitch(MUSIC_SWITCH_STATE, isChecked)

            if (isChecked) {
                val thumbColor = ContextCompat.getColor(this, R.color.rosa)
                val trackColor = ContextCompat.getColor(this, R.color.rosa)

                mediaPlayer = MediaPlayer.create(this, R.raw.waitingtime)
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

    fun irPerfilUsuario(view: View){
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in_config_perfil, R.anim.fade_out);
    }

    private fun vibrarDispositivo() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

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

    fun actualizarContrasena(){
        mediaPlayer.start()
        if (contrasenaAnterior == contrasenaNueva){
            Toast.makeText(this, "Las contraseñas introducidas son iguales", Toast.LENGTH_SHORT).show()
        }
        else{
            UserDao.actualizarContrasena(FirebaseDB.getInstanceFirebase().currentUser?.email.toString(), contrasenaNueva.toString())
            Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarEstadoSwitch(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun guardarEstadoSonidosSwitch(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("sonidosSwitchState", value)
        editor.apply()
    }

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

    fun cerrarSesionConfig(view: View){
        mediaPlayer.start()
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

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