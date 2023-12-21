package com.htnguyen.ihealthclub.view.mainscreen.personal

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.base.BaseViewModel
import com.htnguyen.ihealthclub.model.User


class PersonalProfileViewModel : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData(null)
    val phone: MutableLiveData<String> = MutableLiveData(null)
    val idUser: MutableLiveData<String> = MutableLiveData(null)
    val userName: MutableLiveData<String> = MutableLiveData(null)
    val userPhotoUrl: MutableLiveData<String> = MutableLiveData(null)
    val userBirthDay: MutableLiveData<Long> = MutableLiveData(0)
    val userGender: MutableLiveData<Boolean> = MutableLiveData(null)
    val userHeight: MutableLiveData<Float> = MutableLiveData(null)
    val userWeight: MutableLiveData<Float> = MutableLiveData(null)

    fun getDataProfileUser() {
        val db = Firebase.firestore
        idUser.value?.let {
            db.collection("User").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            userName.value = user.name.toString()
                            userPhotoUrl.value = user.photoUrl.toString()
                            userBirthDay.value = user.birthDay!!
                            userGender.value = user.gender!!
                            userHeight.value = user.height!!
                            userWeight.value = user.weight!!
                        }

                    }
                }
                .addOnFailureListener { exception ->

                }
        }
    }

}