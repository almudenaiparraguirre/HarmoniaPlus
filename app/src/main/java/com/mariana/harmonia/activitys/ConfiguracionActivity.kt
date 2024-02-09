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

        val switchEfectosSonido = findViewById<Switch>(R.id.switchMusica)

        switchEfectosSonido.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mediaPlayer = MediaPlayer.create(this, R.raw.waitingtime)
                mediaPlayer.start()
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.waitingtime)
                mediaPlayer.pause()
                mediaPlayer.seekTo(0) // Reinicia la reproducción al principio
            }
        }

        /*val spinner: Spinner = findViewById(R.id.themeSpinner)
        val themeOptions = resources.getStringArray(R.array.theme_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themeOptions)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter*/
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
                ContextCompat.getColor(this, R.color.rosa_claro)
            } else {
                ContextCompat.getColor(this, R.color.grisClaro)
            }

            val trackColor = if (isChecked) {
                ContextCompat.getColor(this, R.color.rosa_claro)
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