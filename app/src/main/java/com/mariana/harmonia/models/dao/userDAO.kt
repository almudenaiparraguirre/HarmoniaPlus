import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.models.entity.User

class UserDao {

    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("usuarios")

        //AÑADE USUARIO
        fun addUser(user: User) {
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

    }
}