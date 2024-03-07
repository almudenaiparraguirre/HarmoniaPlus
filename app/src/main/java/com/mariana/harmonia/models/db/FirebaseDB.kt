package com.mariana.harmonia.models.db

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseDB {
    companion object {
        @Volatile
        private var INSTANCESTORE: FirebaseFirestore? = null
        private var INSTANCEAUTH: FirebaseAuth? = null
        private var INSTANCESTORAGE: FirebaseStorage? = null

        fun getInstanceFirestore(): FirebaseFirestore {
            synchronized(this) {
                if (INSTANCESTORE == null)
                    INSTANCESTORE = FirebaseFirestore.getInstance()
                return INSTANCESTORE as FirebaseFirestore
            }
        }

        fun getInstanceFirebase(): FirebaseAuth {
            synchronized(this) {
                if (INSTANCEAUTH == null)
                    INSTANCEAUTH = FirebaseAuth.getInstance()
                return INSTANCEAUTH as FirebaseAuth
            }
        }


        fun getInstanceStorage(): FirebaseStorage {
            synchronized(this) {
                if (INSTANCESTORAGE == null)
                    INSTANCESTORAGE = FirebaseStorage.getInstance()
                return INSTANCESTORAGE as FirebaseStorage
            }
        }
    }
}