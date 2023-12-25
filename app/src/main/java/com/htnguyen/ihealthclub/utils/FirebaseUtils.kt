package com.htnguyen.ihealthclub.utils

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.model.*

object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = Firebase.auth.currentUser

    @SuppressLint("StaticFieldLeak")
    val db: FirebaseFirestore = Firebase.firestore
    val database = Firebase.database
    val databaseUser = database.getReference("User")
    val databaseUserLogin = database.getReference("UserLogin")

    private val dataBaseReference: DatabaseReference = Firebase.database.reference
    val databasePost = dataBaseReference.child("Posts")
    val databaseStory= dataBaseReference.child("Stories")
    val databasePostLike = dataBaseReference.child("PostLike")

    fun isUserLogin(
        account: String,
        password: String,
        onSuccess: (userLogin: UserLogin) -> Unit = {},
        onFailure: (exception: Exception) -> Unit = {}
    ) {
        db.collection("UserLogin").document(account).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<UserLogin>()
                    if (user?.password == password) {
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

    fun getUserById(
        idUser: String,
        onSuccess: (user: User) -> Unit = {},
        onFailure: (exception: Exception) -> Unit = {}
    ) {
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

    fun getReactionPost(
        idPost: String,
        idUser: String,
        onSuccess: (userAction: UserAction) -> Unit = {},
        onSuccessTotalLike: (totalLike: Long) -> Unit = {},
        onFailure: (exception: Exception) -> Unit = {}
    ) {
        databasePostLike.child(idPost).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listUserAction = dataSnapshot.childrenCount
                onSuccessTotalLike(listUserAction)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        databasePostLike.child(idPost).child(idUser)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userAction = dataSnapshot.getValue<UserAction>()
                    if (userAction != null)
                        onSuccess(userAction)
                    else
                        onFailure(java.lang.Exception("Can not load data"))
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }

    fun getAllPost(onGetPostSuccess: (listPost: MutableList<Post>) -> Unit = {}) {
        val listPost = mutableListOf<Post>()
        databasePost.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listPost.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val idPost = postSnapshot.child("idPost").getValue(String::class.java)!!
                    val idUser = postSnapshot.child("idUser").getValue(String::class.java)!!
                    val emojiStatus =
                        postSnapshot.child("emojiStatus").getValue(String::class.java)!!
                    val bodyStatus = postSnapshot.child("bodyStatus").getValue(String::class.java)!!
                    val createAt = postSnapshot.child("createAt").getValue(Long::class.java)!!
                    val typePost = postSnapshot.child("typePost").getValue(TypeFile::class.java)!!
                    val likeTotal = postSnapshot.child("likeTotal").getValue(Int::class.java)!!
                    val commentTotal =
                        postSnapshot.child("commentTotal").getValue(Int::class.java)!!
                    val shareTotal = postSnapshot.child("shareTotal").getValue(Int::class.java)!!
                    val listFile = postSnapshot.child("listFile").getValue<List<String>>()!!
                    val listLike = arrayListOf<UserAction>()
                    val listLikeSnapshot = postSnapshot.child("listLike")
                    for (actionLike in listLikeSnapshot.children) {
                        val like = actionLike.getValue<UserAction>()!!
                        listLike.add(like)
                    }
                    listPost.add(
                        Post(
                            idPost,
                            idUser,
                            emojiStatus,
                            bodyStatus,
                            createAt,
                            typePost,
                            likeTotal,
                            commentTotal,
                            shareTotal,
                            listFile
                        )
                    )
                }

                onGetPostSuccess(listPost)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

}