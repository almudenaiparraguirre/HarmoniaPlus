package com.mariana.harmonia.models.db

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * Clase de utilidad para obtener instancias de Firebase Firestore, Auth y Storage.
 * Utiliza el patrón Singleton para garantizar una única instancia de cada servicio.
 */
class FirebaseDB {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCESTORE: FirebaseFirestore? = null
        private var INSTANCEAUTH: FirebaseAuth? = null
        private var INSTANCESTORAGE: FirebaseStorage? = null

        /**
         * Obtiene y devuelve la instancia única de Firebase Firestore.
         *
         * @return Instancia de FirebaseFirestore.
         */
        fun getInstanceFirestore(): FirebaseFirestore {
            synchronized(this) {
                if (INSTANCESTORE == null)
                    INSTANCESTORE = FirebaseFirestore.getInstance()
                return INSTANCESTORE as FirebaseFirestore
            }
        }

        /**
         * Obtiene y devuelve la instancia única de FirebaseAuth.
         *
         * @return Instancia de FirebaseAuth.
         */
        fun getInstanceFirebase(): FirebaseAuth {
            synchronized(this) {
                if (INSTANCEAUTH == null)
                    INSTANCEAUTH = FirebaseAuth.getInstance()
                return INSTANCEAUTH as FirebaseAuth
            }
        }

        /**
         * Obtiene y devuelve la instancia única de FirebaseStorage.
         *
         * @return Instancia de FirebaseStorage.
         */
        fun getInstanceStorage(): FirebaseStorage {
            synchronized(this) {
                if (INSTANCESTORAGE == null)
                    INSTANCESTORAGE = FirebaseStorage.getInstance()
                return INSTANCESTORAGE as FirebaseStorage
            }
        }
    }
}