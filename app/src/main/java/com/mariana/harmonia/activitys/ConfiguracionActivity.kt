package com.mariana.harmonia.activitys

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.databinding.InicioSesionActivityBinding

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var buttonCambiarContra: Button
    private lateinit var switchMusica: Switch
    private lateinit var switchOtraOpcion: Switch
    private lateinit var textViewEliminarCuenta: TextView
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configuracion_activity)
        switchMusica = findViewById(R.id.switchMusica)
        switchOtraOpcion = findViewById(R.id.switchSonidos)
        buttonCambiarContra = findViewById(R.id.buttonCambiarContrasena)
        textViewEliminarCuenta = findViewById(R.id.textViewEliminarCuenta)

        configurarSwitchColor(switchMusica)
        configurarSwitchColor(switchOtraOpcion)

        buttonCambiarContra.setOnClickListener {
            mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
            mediaPlayer.start()
        }

        textViewEliminarCuenta.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        switchMusica.setOnCheckedChangeListener { _, isChecked ->
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

                if (mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                }

                switchMusica.thumbTintList = ColorStateList.valueOf(thumbColor)
                switchMusica.trackTintList = ColorStateList.valueOf(trackColor)
            }
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Cuenta")
        builder.setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer y perderás todos los progresos")
        builder.setPositiveButton("Sí") { _: DialogInterface, _: Int ->
            eliminarMiCuenta()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
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

    fun volverModoJuego(view: View){
        finish()
    }

    fun cerrarSesionConfig(view: View){
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun eliminarMiCuenta() {
        auth.signOut()
        Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}