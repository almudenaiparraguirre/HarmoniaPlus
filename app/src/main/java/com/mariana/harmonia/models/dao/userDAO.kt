import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.models.entity.User

class UserDao {

    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("usuarios")


        //CREA EL USUARIO
        fun createUsersCollectionIfNotExists() {
            usersCollection.get().addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // La colección de usuarios está vacía, así que la creamos
                    db.collection("usuarios").document("init").set(hashMapOf("init" to true))
                        .addOnSuccessListener {
                            Log.d(TAG, "Colección 'usuarios' creada exitosamente")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error al crear la colección 'usuarios'", e)
                        }
                }
            }
        }

        //AÑADE USUARIO

        fun addUser(user: User) {
            // Agregar un usuario a la base de datos
            val emailKey = user.email?.replace(".", ",")
            emailKey?.let {
                usersCollection.document(it).set(user)
                    .addOnSuccessListener { Log.d(TAG, "Usuario agregado con email: $emailKey") }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error al agregar usuario con email: $emailKey", e)
                    }
            }
        }

        fun getUserField(email: String?, fieldName: String, onSuccess: (Any) -> Unit, onFailure: (Exception) -> Unit) {
            if (email != null) {
                usersCollection.document(email).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val field = document.get(fieldName)
                            if (field != null) {
                                onSuccess.invoke(field)
                            } else {
                                onFailure.invoke(Exception("El campo $field no existe en el documento"))
                            }
                        } else {
                            onFailure.invoke(Exception("No se encontró el documento para el usuario con email: $email"))
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure.invoke(e)
                    }
            }
        }
        // Método para devolver nivel y experiencia sobrante


        fun eliminarUsuario(email: String){
            val emailKey = email.replace(".", ",")

            usersCollection.document(emailKey).delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Usuario eliminado con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al eliminar usuario con email: $email", e)
                }
        }

        fun actualizarContrasena(email: String, nuevaContrasena: String) {
            val emailKey = email.replace(".", ",")

            val data = hashMapOf(
                "contrasena" to nuevaContrasena
                // Agrega cualquier otro campo que necesites actualizar
            )

            usersCollection.document(emailKey).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d(TAG, "Contraseña actualizada para el usuario con email: $email")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al actualizar contraseña para el usuario con email: $email", e)
                }
        }
        fun getEmail(email: String){
            val db = FirebaseFirestore.getInstance()
            val usuarios = db.collection("usuarios")
            val stateQuery = usuarios.whereEqualTo("email", email)
            // Ejecutar la consulta y obtener el resultado
            stateQuery.get().addOnSuccessListener { querySnapshot ->
                println("Consulta exitosa. Documentos encontrados: ${querySnapshot.size()}")
                // Recorrer los documentos obtenidos
                for (document in querySnapshot.documents) {
                    // Obtener el nombre del usuario y imprimirlo por consola
                    val nombre = document.getString("email")
                    println("Nombre: $nombre")
                }
            }.addOnFailureListener { exception ->
                // Manejar cualquier error que ocurra al ejecutar la consulta
                println("Error al obtener los usuarios: $exception")
            }
        }

        fun getVidas(email: String?, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit) {
            getUserField(email, "vidas",
                onSuccess = { field ->
                    if (field is Long) {
                        onSuccess.invoke(field.toInt()) // Convertir a Int si es Long
                    } else {
                        onFailure.invoke(Exception("El campo 'vidas' no es un entero"))
                    }
                },
                onFailure = onFailure
            )
        }
    }
}
