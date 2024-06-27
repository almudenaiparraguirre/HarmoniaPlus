package com.mariana.harmonia.activities.pantallasExtras

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mariana.harmonia.R
import com.mariana.harmonia.activities.ConfiguracionActivity
import com.mariana.harmonia.activities.PerfilUsuarioActivity

class PersonalizacionActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var borderViews: List<View>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.personalizacion_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)

        sharedPreferences = getSharedPreferences("com.mariana.harmonia", MODE_PRIVATE)

        // Referencias a los borderView
        val borderView1: View = findViewById(R.id.borderView1)
        val borderView2: View = findViewById(R.id.borderView2)
        val borderView3: View = findViewById(R.id.borderView3)
        val borderView4: View = findViewById(R.id.borderView4)
        val borderView5: View = findViewById(R.id.borderView5)
        val borderView6: View = findViewById(R.id.borderView6)
        val borderView7: View = findViewById(R.id.borderView7)
        val borderView8: View = findViewById(R.id.borderView8)

        borderViews = listOf(borderView1, borderView2, borderView3, borderView4, borderView5, borderView6, borderView7, borderView8)

        // Referencias a los colores internos
        val color1: View = findViewById(R.id.color1)
        val color2: View = findViewById(R.id.color2)
        val color3: View = findViewById(R.id.color3)
        val color4: View = findViewById(R.id.color4)
        val color5: View = findViewById(R.id.color5)
        val color6: View = findViewById(R.id.color6)
        val color7: View = findViewById(R.id.color7)
        val color8: View = findViewById(R.id.color8)

        // Asignar el listener de clic para cambiar el fondo y actualizar el color principal
        setClickListener(color1, borderView1, R.color.rosa)
        setClickListener(color2, borderView2, R.color.rosaClaro)
        setClickListener(color3, borderView3, R.color.rojo)
        setClickListener(color4, borderView4, R.color.rojoClaro)
        setClickListener(color5, borderView5, R.color.azul)
        setClickListener(color6, borderView6, R.color.azulClaro)
        setClickListener(color7, borderView7, R.color.amarillo)
        setClickListener(color8, borderView8, R.color.verde)
    }

    private fun setClickListener(colorView: View, associatedBorderView: View, colorResId: Int) {
        colorView.setOnClickListener {
            // Cambiar el fondo del marco asociado
            associatedBorderView.setBackgroundResource(R.drawable.redondeado_color_esquinas)

            // Poner en marco blanco todos los demás
            borderViews.forEach { borderView ->
                if (borderView != associatedBorderView) {
                    borderView.setBackgroundResource(R.drawable.redondeado_blanco_esquinas)
                }
            }

            // Guardar el color seleccionado en SharedPreferences
            val color = ContextCompat.getColor(this, colorResId)
            saveColorPrincipal(color)
        }
    }

    private fun saveColorPrincipal(newColor: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("color_principal", newColor)
        editor.apply()
    }

    fun irPerfilUsuario(view: View) {
        mediaPlayer.start()
        val intent = Intent(this, PerfilUsuarioActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in_config_perfil, R.anim.fade_out)
    }

    fun irConfiguracion(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.sonido_cuatro)
        mediaPlayer.start()
        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in_config_perfil, R.anim.fade_out)
    }
}
