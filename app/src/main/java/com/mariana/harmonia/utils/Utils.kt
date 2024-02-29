package com.mariana.harmonia.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.R
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream

class Utils {
    companion object {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
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


        fun serializeImage(context: Context, resourceId: Int) {
            try {
                val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imageBytes: ByteArray = outputStream.toByteArray()
                outputStream.close()
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)


                val file =
                    File(context.resources.openRawResource(R.raw.imagen_serializada).toString())

                // Lee el contenido actual del archivo JSON
                val json = readJsonFromRaw(R.raw.imagen_serializada, context)

                // Parsea el contenido JSON a un objeto JSONObject
                val existingJsonObj = JSONObject(json)

                // Agrega el nuevo dato ("Hola mundo") al JSON
                existingJsonObj.put("IMAGEN_SERIALIZADA", base64Image.toString())

                // Escribe el JSON modificado de vuelta al archivo
                val fileWriter = FileWriter(file)
                fileWriter.write(existingJsonObj.toString())
                fileWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }


        fun readJsonFromRaw(resourceId: Int, context: Context): String {
            val inputStream: InputStream = context.resources.openRawResource(resourceId)
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            return json
        }
        fun deserializeImage(jsonString: String, destinationPath: String): Boolean {
            try {
                val jsonObject = JSONObject(jsonString)
                val base64Image = jsonObject.getString("imagen_serializada")

                val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val file = File(destinationPath)
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(imageBytes)
                fileOutputStream.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }



    }
}
