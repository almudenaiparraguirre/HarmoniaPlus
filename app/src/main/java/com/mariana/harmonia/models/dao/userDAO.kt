package com.mariana.harmonia.models.dao

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mariana.harmonia.models.entity.User

class UserDao {

    companion object {
        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        fun addUser(user: User) {
            // Agregar un usuario a la base de datos
            val emailKey = user.email?.replace(".", ",")
            emailKey?.let {
                databaseReference.child(it).setValue(user)
            }
        }

        fun updateUser(user: User) {
            // Actualizar un usuario en la base de datos
            val emailKey = user.email?.replace(".", ",")
            emailKey?.let {
                databaseReference.child(it).setValue(user)
            }
        }

        fun deleteUser(email: String) {
            // Eliminar un usuario de la base de datos
            val emailKey = email.replace(".", ",")
            databaseReference.child(emailKey).removeValue()
        }
    }
}
