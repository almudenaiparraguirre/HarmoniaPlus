package com.mariana.harmonia.models.db

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseDB {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCESTORE: FirebaseFirestore? = null
        private var INSTANCEAUTH: FirebaseAuth? = null
        private var INSTANCESTORAGE: FirebaseStorage? = null
        private var INSTANCEANALYTICS: FirebaseAnalytics? = null

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

        fun getInstanceAnalytics(context: Context): FirebaseAnalytics {
            synchronized(this) {
                if (INSTANCEANALYTICS == null) {
                    INSTANCEANALYTICS = FirebaseAnalytics.getInstance(context)
                }
                return INSTANCEANALYTICS as FirebaseAnalytics
            }
        }

    }
}