package com.mariana.harmonia.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mariana.harmonia.models.db.FirebaseDB
import kotlinx.coroutines.tasks.await
import java.time.format.DateTimeFormatter
import java.util.Locale

class UtilsDB {
    companion object {
        var firebaseAuth = FirebaseDB.getInstanceFirebase()
        var db = FirebaseDB.getInstanceFirestore()
        var currentUser = firebaseAuth.currentUser
        var email = currentUser?.email?.lowercase()
        var usersCollection = db.collection("usuarios")
        var emailEncriptado = HashUtils.sha256(email!!)

        fun actualizarVariables() {
            try {
                firebaseAuth = FirebaseDB.getInstanceFirebase()
                db = FirebaseDB.getInstanceFirestore()
                currentUser = firebaseAuth.currentUser
                email = currentUser?.email?.lowercase()
                usersCollection = db.collection("usuarios")
                emailEncriptado = HashUtils.sha256(email!!)
            } catch (e: NullPointerException) {
                Log.e(ContentValues.TAG, "Error de NullPointerException al actualizar variables: $e")
            }
        }

        suspend fun getCorreo(): String? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)
            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("correo")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }

        suspend fun getNombre(): String? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("name")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }


        suspend fun getExperiencia(): Int? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val experiencia = document.data?.get("experiencia") as? Long
                    experiencia?.toInt()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }


        suspend fun getNivelActual(): Int? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("nivelActual")
                    println("Nivel actual: $nivelActual")
                    (nivelActual as? Long)?.toInt() // Convertir a Int
                } else {
                    println("No existe el documento")
                    null
                }
            } catch (exception: Exception) {
                println("Error al obtener documentos: $exception")
                null
            }
        }

        suspend fun getVidas(): String? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val nivelActual = document.data?.get("vidas")
                    println("Nivel actual: $nivelActual")
                    nivelActual?.toString()
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }

        suspend fun getPrecisiones(): List<Int>? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val precisionesList = document.data?.get("precisiones") as? List<Int>
                    println("precisiones: $precisionesList")
                    precisionesList
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }

        suspend fun getPuntuacionDesafio(): Pair<Int, Int>? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val precisionesList = document.data?.get("precisiones") as? List<Int>
                    println("precisiones: $precisionesList")

                    if (precisionesList != null && precisionesList.size >= 2) {
                        Pair(precisionesList[0], precisionesList[1])
                    } else {
                        null
                    }
                } else {
                    println("No such document")
                    null
                }
            } catch (exception: Exception) {
                println("Error getting documents: $exception")
                null
            }
        }
        suspend fun getTiempoJugado(): Int? {
            actualizarVariables()
            val docRef = db.collection("usuarios").document(emailEncriptado)

            return try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val tiempo = document.data?.get("tiempoJugado")
                    println("Nivel actual: $tiempo")
                    (tiempo as? Long)?.toInt() // Convertir a Int
                } else {
                    println("No existe el documento")
                    null
                }
            } catch (exception: Exception) {
                println("Error al obtener documentos: $exception")
                null
            }
        }



        @SuppressLint("SimpleDateFormat")
        fun obtenerFechaActualEnTexto(): String {
            actualizarVariables()
            val fechaActual: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para versiones de Android 8.0 y superiores
                val currentDate = java.time.LocalDate.now()
                val formatter = java.time.format.DateTimeFormatter.ofPattern(
                    "MMMM 'de' yyyy",
                    Locale.getDefault()
                )
                fechaActual = currentDate.format(formatter)
            } else {
                // Para versiones anteriores a Android 8.0
                val currentDate = Calendar.getInstance().time
                val formato = SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault())
                fechaActual = formato.format(currentDate)
            }

            return fechaActual
        }


        fun setCorreo(correo: String) {
            actualizarVariables()
            val data = hashMapOf(
                "correo" to correo
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Correo actualizado para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error al actualizar correo para el usuario con email: $email",
                        e
                    )
                }
        }

        fun setExperiencia(nuevaExperiencia: Int) {
            actualizarVariables()
            val data = hashMapOf(
                "experiencia" to nuevaExperiencia
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "Experiencia actualizada para el usuario con email: $email"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error al actualizar experiencia para el usuario con email: $email",
                        e
                    )
                }
        }

        fun setNivelActual(nuevoNivelActual: Int) {
            actualizarVariables()
            val data = hashMapOf(
                "nivelActual" to nuevoNivelActual
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "NivelActual actualizada para el usuario con email: $email"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error al actualizar NivelActual para el usuario con email: $email",
                        e
                    )
                }
        }

        fun setVidas(nuevaVidas: Int) {
            actualizarVariables()
            val data = hashMapOf(
                "vidas" to nuevaVidas
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "nuevaVidas actualizada para el usuario con email: $email"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error al actualizar nuevaVidas para el usuario con email: $email",
                        e
                    )
                }
        }
        fun setTiempoJugado(tiempo: Int) {
            actualizarVariables()

            if (emailEncriptado != null) {
                val data = hashMapOf(
                    "tiempoJugado" to tiempo
                    // Agrega cualquier otro campo que necesites actualizar
                )

                usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "nuevaVidas actualizada para el usuario con email: $email"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            ContentValues.TAG,
                            "Error al actualizar nuevaVidas para el usuario con email: $email",
                            e
                        )
                    }
            } else {
                Log.w(ContentValues.TAG, "Email encriptado es nulo, no se puede actualizar el tiempo jugado.")
            }
        }

        fun setPrecisiones(precisiones: List<Int>) {
            actualizarVariables()
            val data = hashMapOf(
                "precisiones" to precisiones
            )
            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Precisión actualizada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar la precisión", e)
                }
        }

        fun setPuntuacionDesafio(puntuacion: Pair<Int, Int>) {
            actualizarVariables()
            val data = hashMapOf(
                "puntuacionDesafio" to puntuacion
            )
            usersCollection.document(emailEncriptado).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "PuntuacionDesafio actualizada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar la PuntuacionDesafio", e)
                }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setPuntuacionDesafioGlobal(puntuacion: Pair<Int, Int>) {
            actualizarVariables()
            val desafioCollection = db.collection("desafio")
            val currentDateTime = java.time.LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss")
            val fechaActual = currentDateTime.format(formatter) + " User=" + currentUser?.email
            println(fechaActual)
            val data = hashMapOf(
                fechaActual to puntuacion
            )
            desafioCollection.document("puntuaciones").update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "PuntuacionDesafio actualizada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error al actualizar la PuntuacionDesafio", e)
                }
        }


        suspend fun getMediaPrecisiones(): Int {
            actualizarVariables()
            val precisionesList = getPrecisiones() ?: return 0

            if (precisionesList.isNotEmpty()) {
                var suma = 0
                var contador = 0

                for (precision in precisionesList) {
                    if (precision != 0) {
                        suma += precision
                        contador++
                    }
                }

                return if (contador > 0) {
                    suma / contador
                } else {
                    0
                }
            }

            return 0
        }
    }
}