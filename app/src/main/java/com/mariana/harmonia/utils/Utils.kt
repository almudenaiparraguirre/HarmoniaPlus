package com.mariana.harmonia.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.R
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class Utils {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser


        @SuppressLint("SimpleDateFormat")
        fun obtenerFechaActualEnTexto(): String {
            val fechaActual: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para versiones de Android 8.0 y superiores
                val currentDate = java.time.LocalDate.now()
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault())
                fechaActual = currentDate.format(formatter)
            } else {
                // Para versiones anteriores a Android 8.0
                val currentDate = Calendar.getInstance().time
                val formato = SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault())
                fechaActual = formato.format(currentDate)
            }

            return fechaActual
        }

        fun obtenerVidas(experiencia: TextView?) {

            val emailFire = currentUser?.email
            val email = emailFire?.replace(".", ",")

            try {
                UserDao.getUserField(email, "vidas",
                    onSuccess = { name ->
                        experiencia?.post {
                            experiencia.text = name.toString() as? CharSequence ?: ""
                            Toast.makeText(experiencia.context, name as? CharSequence ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error al obtener el nombre del modo de juego: ${exception.message}",
                        exception
                    )
                    experiencia?.post {
                        experiencia?.text = "unnamed"
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
                experiencia?.post {
                    experiencia?.text = "unnamed"
                }
            }
        }

            fun obtenerNombre(nombre: TextView?) {

                val emailFire = currentUser?.email
                val email = emailFire?.replace(".", ",")

                try {
                    UserDao.getUserField(email, "name",
                        onSuccess = { name ->
                            nombre?.post {
                                nombre.text = name as? CharSequence ?: ""
                                Toast.makeText(nombre.context, name as? CharSequence ?: "", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) { exception ->
                        Log.e(
                            ContentValues.TAG,
                            "Error al obtener el nombre del modo de juego: ${exception.message}",
                            exception
                        )
                        nombre?.post {
                            nombre?.text = "unnamed"
                        }
                    }
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
                    nombre?.post {
                        nombre?.text = "unnamed"
                    }
                }
            }

        fun obtenerNivel(nivel: TextView?) {

            val emailFire = currentUser?.email
            val email = emailFire?.replace(".", ",")

            try {
                UserDao.getUserField(email, "experiencia",
                    onSuccess = { name ->
                        nivel?.post {
                            nivel.text = "NV." + (name.toString().toInt()/100).toString() as? CharSequence ?: ""
                            Toast.makeText(nivel.context, name as? CharSequence ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error al obtener el nombre del modo de juego: ${exception.message}",
                        exception
                    )
                    nivel?.post {
                        nivel?.text = "unnamed"
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
                nivel?.post {
                    nivel?.text = "unnamed"
                }
            }
        }
        fun obtenerExperiencia(progressBar: ProgressBar?) {

            val emailFire = currentUser?.email
            val email = emailFire?.replace(".", ",")

            try {
                UserDao.getUserField(email, "experiencia",
                    onSuccess = { experiencia ->
                        progressBar?.post {
                            // Suponiendo que la experiencia es un valor entre 0 y 100 y deseas mostrarlo en el ProgressBar
                            progressBar.progress = experiencia.toString().toInt()%100
                            Toast.makeText(progressBar.context, "Experiencia: $experiencia", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error al obtener la experiencia del usuario: ${exception.message}",
                        exception
                    )
                    progressBar?.post {
                        // Aquí puedes decidir qué hacer en caso de error, como mantener el valor actual del ProgressBar o reiniciarlo
                        // Por ejemplo, si quieres mantener el valor actual:
                        // progressBar.progress = progressBar.progress
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Excepción al obtener la experiencia del usuario: ${e.message}", e)
                progressBar?.post {
                    // Aquí puedes decidir qué hacer en caso de excepción, como mantener el valor actual del ProgressBar o reiniciarlo
                    // Por ejemplo, si quieres reiniciar el ProgressBar:
                    // progressBar.progress = 0
                }
            }
        }

        fun obtenerExperienciaTotal(experiencia: TextView?) {

            val emailFire = currentUser?.email
            val email = emailFire?.replace(".", ",")

            try {
                UserDao.getUserField(email, "experiencia",
                    onSuccess = { name ->
                        experiencia?.post {
                            experiencia.text = name.toString() as? CharSequence ?: ""
                            Toast.makeText(experiencia.context, name as? CharSequence ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { exception ->
                    Log.e(
                        ContentValues.TAG,
                        "Error al obtener el nombre del modo de juego: ${exception.message}",
                        exception
                    )
                    experiencia?.post {
                        experiencia?.text = "unnamed"
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Excepción al obtener el nombre del modo de juego: ${e.message}", e)
                experiencia?.post {
                    experiencia?.text = "unnamed"
                }
            }
        }

        fun actualizarCorreo(correoTextView: TextView?){
            val emailFire = currentUser?.email
            correoTextView?.text = emailFire

        }
        fun readJsonFromRaw(resourceId: Int, context: Context): String {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            return json
        }

        fun serializeImage(context: Context, resourceId: Int) {
            try {
                val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageBytes: ByteArray = outputStream.toByteArray()
                outputStream.close()
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                val enviroment = Environment.getExternalStorageDirectory()
                val textFile = File(enviroment, "Download/imagenSerializada.json")

                try {
                    val fos = FileOutputStream(textFile)
                    fos.write(base64Image.toByteArray())
                    fos.close()
                } catch (e: IOException) {
                   println( e.toString())
                    // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
                }
            } catch (e: IOException) {
                e.printStackTrace()

                // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
            }
        }




        fun deserializeImage(context: Context, jsonFilePath: String): Bitmap? {
            try {
                val jsonFile = File(jsonFilePath)
                if (!jsonFile.exists()) {
                    println("El archivo JSON no existe.")
                    return null
                }

                val inputStream: InputStream = FileInputStream(jsonFile)
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                val imageBytes: ByteArray = Base64.decode(jsonString, Base64.DEFAULT)
                val inputStreamImage: InputStream = ByteArrayInputStream(imageBytes)
                return BitmapFactory.decodeStream(inputStreamImage)
            } catch (e: IOException) {
                e.printStackTrace()
                println(e.toString())
                // Manejar la excepción, por ejemplo, mostrar un mensaje al usuario
            }
            return null
        }


         fun isExternalStorageWritable(): Boolean {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                Log.i("State", "Yes, it is writable!")
                println("Yes, it is writable!")
                return true
            }else{
                Log.i("State", "Caution, it's not writable!")
                println("Caution, it's not writable!")
            }
            return false
        }

         fun isExternalStorageReadable(): Boolean {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
                Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()) {
                Log.i("State", "Yes, it is readable!")
                println("Yes, it is readable!")
                return true
            }else{
                Log.i("State", "Caution, it's not readable!")
                println("Caution, it's not readable!")
            }
            return false
        }
    }
}
