package com.mariana.harmonia.activitys

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mariana.harmonia.R
/*import com.mariana.harmonia.databinding.ActivityMain2Binding*/
import com.mariana.harmonia.databinding.MainActivityBinding

class PerfilUsuarioActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    private lateinit var binding: MainActivityBinding
    private lateinit var imagen: ImageView
    private lateinit var lapiz: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            //val message = if (isGranted) "Permission Granted" else "Permission rejected"
            //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (isGranted) {
                abrirGaleria()
            } else {
                abrirGaleria()
                Toast.makeText(this, "Necesitas activar los permisos de la galería", Toast.LENGTH_SHORT).show()
            }
        }

    // FUN --> OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_usuario_activity)
       // cargarEstadisticasLogros()
        val cardViewPerfil = findViewById<CardView>(R.id.cardview_perfil)
        imagen = findViewById(R.id.roundedImageView)
        val fondoMitadSuperior = findViewById<ImageView>(R.id.roundedImageView)
        lapiz = findViewById(R.id.lapiz_editar)
        val fondoMitadSuperiorBack = findViewById<ImageView>(R.id.fondoMitadSuperiorBackground)

        cardViewPerfil.setOnClickListener {
            mostrarDialogImagen(imagen)
        }
        binding = MainActivityBinding.inflate(layoutInflater)
       lapiz.setOnClickListener { requestPermission() }

        // Obtener el color promedio de la imagen
        val bitmap = (imagen.drawable as BitmapDrawable).bitmap
        val colorPromedio = obtenerColorPromedio(bitmap)

        //Este lIstener hace que al clicar fuera de la pantalla se deje de rellenar el campo del nombre
        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayoutID) // Reemplaza con el ID de tu ConstraintLayout
        val editText: EditText = findViewById(R.id.nombre_usuario) // Reemplaza con el ID de tu EditText

        constraintLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Oculta el teclado
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)

                // Quita el foco del EditText
                editText.clearFocus()
            }
            false
        }
// Porcentaje barra Experiencia
        val progressBar1 = findViewById<ProgressBar>(R.id.progressBarLogro1)
        val porcentajeTextView1 = findViewById<TextView>(R.id.TextViewLogro1)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBarLogro2)
        val porcentajeTextView2 = findViewById<TextView>(R.id.TextViewLogro2)
        val progressBar3 = findViewById<ProgressBar>(R.id.progressBarLogro3)
        val porcentajeTextView3 = findViewById<TextView>(R.id.TextViewLogro3)
        val progressBar4 = findViewById<ProgressBar>(R.id.progressBarLogro4)
        val porcentajeTextView4 = findViewById<TextView>(R.id.TextViewLogro4)
        val progressBar5 = findViewById<ProgressBar>(R.id.progressBarLogro5)
        val porcentajeTextView5 = findViewById<TextView>(R.id.TextViewLogro5)
        val progressBar6 = findViewById<ProgressBar>(R.id.progressBarLogro6)
        val porcentajeTextView6 = findViewById<TextView>(R.id.TextViewLogro6)
        val progressBar7= findViewById<ProgressBar>(R.id.progressBarLogro7)
        val porcentajeTextView7 = findViewById<TextView>(R.id.TextViewLogro7)
        val progressBar8 = findViewById<ProgressBar>(R.id.progressBarLogro8)
        val porcentajeTextView8 = findViewById<TextView>(R.id.TextViewLogro8)

        // Puedes actualizar el porcentaje directamente
        val porcentaje1 = 10 // ajusta esto a tu valor real de porcentaje
        progressBar1.progress = porcentaje1
        porcentajeTextView1.text = "$porcentaje1/100"

        val porcentaje2 = 20
        progressBar2.progress = porcentaje2
        porcentajeTextView2.text = "$porcentaje2/100"

        val porcentaje3 = 30
        progressBar3.progress = porcentaje3
        porcentajeTextView3.text = "$porcentaje3/100"

        val porcentaje4 = 40
        progressBar4.progress = porcentaje4
        porcentajeTextView4.text = "$porcentaje4/100"

        val porcentaje5 = 50
        progressBar5.progress = porcentaje5
        porcentajeTextView5.text = "$porcentaje5/100"

        val porcentaje6 = 60
        progressBar6.progress = porcentaje6
        porcentajeTextView6.text = "$porcentaje6/100"

        val porcentaje7 = 70
        progressBar7.progress = porcentaje7
        porcentajeTextView7.text = "$porcentaje7/100"

        val porcentaje8 = 80
        progressBar8.progress = porcentaje8
        porcentajeTextView8.text = "$porcentaje8/100"


        /* val imageView: ImageView = findViewById(R.id.fondoImageView)

         // Cargar la animación desde el archivo XML
         val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

         // Aplicar la animación al ImageView
         imageView.startAnimation(anim)*/

    }




    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            abrirGaleria()
        } else {
            // Solicitar permisos explícitamente
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun obtenerColorPromedio(bitmap: Bitmap): Int {
        var red = 0
        var green = 0
        var blue = 0
        var pixelCount = 0

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                red += Color.red(pixel)
                green += Color.green(pixel)
                blue += Color.blue(pixel)
                pixelCount++
            }
        }

        red /= pixelCount
        green /= pixelCount
        blue /= pixelCount

        return Color.rgb(red, green, blue)
    }

    // FUN --> Mostrar la imagen del perfil en grande
    private fun mostrarDialogImagen(imageView: ImageView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_imagen)

        val imagenAmpliada = dialog.findViewById<ImageView>(R.id.imagenAmpliada)
        imagenAmpliada.setImageDrawable(imageView.drawable)

        // Animación de escala
        val escala = ScaleAnimation(
            0.2f,  // Escala de inicio
            1.0f,  // Escala de fin
            0.2f,  // Punto focal de inicio (X)
            0.2f,  // Punto focal  de inicio (Y)
            Animation.RELATIVE_TO_SELF, 0.5f,  // Punto focal de fin (X)
            Animation.RELATIVE_TO_SELF, 0.5f   // Punto focal de fin (Y)
        )

        escala.duration = 200  // Duración de la animación en milisegundos
        imagenAmpliada.startAnimation(escala)

        // Cierra el dialog al hacer clic en la imagen
        imagenAmpliada.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun abrirGaleria() {
        //val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    val startForActivityGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                imagen.setImageURI(data)
            }
        }

    fun volverModoJuego(view: View){
        val intent = Intent(this, EligeModoJuegoActivity::class.java)
        startActivity(intent)
        finish()
    }
}