package com.htnguyen.ihealthclub.utils

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.model.UserLogin

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = Firebase.auth.currentUser

    @SuppressLint("StaticFieldLeak")
    val db: FirebaseFirestore = Firebase.firestore
    val database = Firebase.database
    val databaseUser = database.getReference("User")

    val dataBaseReference: DatabaseReference = Firebase.database.reference
    val databasePost = database.getReference("Posts")

    fun isUserLogin(account: String, password: String, onSuccess: (userLogin: UserLogin) -> Unit = {}, onFailure: (exception: Exception) -> Unit = {}){
        db.collection("UserLogin").document(account).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<UserLogin>()
                    if (user?.password == password ) {
                        if (!user.iHealthClub) {
                            db.collection("UserLogin").document(account).update(
                                mapOf(
                                    "iHealthClub" to true
                                )
                            )
                        }
                        onSuccess(user)
                    } else {
                        onFailure(java.lang.Exception("Phone or Password wrong, please check again !"))
                    }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getUserById(idUser: String, onSuccess: (user: User) -> Unit = {}, onFailure: (exception: Exception) -> Unit = {}){
        db.collection("User").document(idUser).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)!!
                    onSuccess(user)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}