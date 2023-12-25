package com.htnguyen.ihealthclub.view.mainscreen.personal

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealthclub.base.BaseViewModel
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import com.htnguyen.ihealthclub.utils.Utils
import java.util.*


class PersonalProfileViewModel : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData(null)
    val phone: MutableLiveData<String> = MutableLiveData(null)
    val idUser: MutableLiveData<String> = MutableLiveData(null)
    val userName: MutableLiveData<String> = MutableLiveData(null)
    val userPhotoUrl: MutableLiveData<String> = MutableLiveData(null)
    val userBirthDay: MutableLiveData<String> = MutableLiveData("--")
    val userBirthDayLong: MutableLiveData<Long> = MutableLiveData(0)
    val userGender: MutableLiveData<Boolean?> = MutableLiveData(null)
    val userHeight: MutableLiveData<String> = MutableLiveData("--")
    val userWeight: MutableLiveData<String> = MutableLiveData("--")

    fun getDataProfileUser() {
        idUser.value?.let {
            FirebaseUtils.getUserById(it,
                onSuccess = {user ->
                    userName.value = user.name.toString()
                    userPhotoUrl.value = user.photoUrl.toString()
                    userBirthDayLong.value = user.birthDay!!
                    userBirthDay.value = if (user.birthDay != null && user.birthDay!! != 0L) Utils.convertDateToString(
                        Date(user.birthDay!!)
                    ) else "--"
                    userGender.value = user.gender
                    userHeight.value = if (user.height != null && user.height!! != 0F) user.height?.toInt().toString() else "--"
                    userWeight.value = if (user.weight != null && user.weight!! != 0F) user.weight?.toInt().toString() else "--"
                },
                onFailure = {

                }
            )

        }
    }

}