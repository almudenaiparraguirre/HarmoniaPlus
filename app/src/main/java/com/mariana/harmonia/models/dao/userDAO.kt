import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.mariana.harmonia.models.entity.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mariana.harmonia.models.db.FirebaseDB

/**
 * Clase que gestiona las operaciones relacionadas con la base de datos de usuarios en Firebase.
 */
class UserDao {

    companion object {
        private val db = FirebaseDB.getInstanceFirestore()
        private val usersCollection = db.collection("usuarios")

        //CREA EL USUARIO
        /*fun createUsersCollectionIfNotExists() {
            usersCollection.get().addOnSuccessListener { documents ->
                if (documents.isEmpty) {
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
*/
        /**
         * Añade un usuario a la colección en Firebase.
         *
         * @param user Usuario a agregar a la base de datos.
         */
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
    }
}