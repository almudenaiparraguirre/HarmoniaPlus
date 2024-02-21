import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.models.entity.User

class UserDao {

    companion object {
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("usuarios")

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

        fun getUserField(email: String, field: String, onSuccess: (Any?) -> Unit, onFailure: (Exception) -> Unit) {
            val emailKey = email.replace(".", ",")
            usersCollection.document(emailKey).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val value = document.get(field)
                        onSuccess(value)
                    } else {
                        onFailure(Exception("El usuario con el email $email no existe"))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }

        // Otros métodos de UserDao...
    }
}
