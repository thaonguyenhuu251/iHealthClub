package com.htnguyen.ihealthclub.view.mainscreen.personal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.view.adapter.PostAdapter
import com.htnguyen.ihealthclub.view.mainscreen.home.CreatePostsActivity
import com.htnguyen.ihealthclub.view.mainscreen.home.PickImageStoryActivity
import com.htnguyen.ihealthclub.base.BaseFragment
import com.htnguyen.ihealthclub.databinding.FragmentPersonalProfileBinding
import com.htnguyen.ihealthclub.model.TypeAction
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.mainscreen.home.PickImageResultActivity
import com.htnguyen.ihealthclub.view.mainscreen.setting.SettingFragment
import kotlinx.android.synthetic.main.fragment_personal_profile.*
import kotlinx.android.synthetic.main.fragment_personal_profile.img_avatar
import java.io.File
import java.io.IOException

class PersonalProfileFragment :
    BaseFragment<FragmentPersonalProfileBinding, PersonalProfileViewModel>() {
    private var postAdapter: PostAdapter? = null
    private val viewModel by viewModels<PersonalProfileViewModel>()

    override val layout: Int
        get() = R.layout.fragment_personal_profile

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getStringExtra(KEY_PATH_IMAGE_POST)

                Glide.with(this).load(data).into(img_avatar)

                val filePath =
                    this@PersonalProfileFragment.context?.let { getRealPathFromUri(it, Uri.parse(data)) }
                uploadFile(filePath!!)
            }
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    override fun onStart() {
        super.onStart()
        val optionsPost: FirebaseRecyclerOptions<Post> =
            FirebaseRecyclerOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(FirebaseUtils.databasePost, Post::class.java)
                .build()
        postAdapter = viewModel.idUser.value?.let {
            PostAdapter(it, requireContext(), callback = {

            }, onActionLike = { reaction, postSelect ->
                val userReactionLike = UserAction(
                    idUser = viewModel.idUser.value,
                    typeAction = reaction.reactTypeAction,
                    timeAction = System.currentTimeMillis(),
                    contentAction = ""
                )
                if (userReactionLike.typeAction != TypeAction.NO)
                    FirebaseUtils.databasePostLike.child(postSelect.idPost)
                        .child(userReactionLike.idUser.toString())
                        .setValue(userReactionLike)
                else
                    FirebaseUtils.databasePostLike.child(postSelect.idPost)
                        .child(userReactionLike.idUser.toString())
                        .setValue(null)
            }, callback3 = { _, _ ->

            }, optionsPost)
        }
        postAdapter?.startListening()
        rv_post_person.adapter = postAdapter
    }

    private fun initData() {
        binding.viewModel = viewModel
        viewModel.idUser.value =
            sharedPreferences.getString(USER_ID, "").toString()

        viewModel.getDataProfileUser()
    }

    private fun initView() {
        if (arguments?.getInt(PERSON_TYPE) == 0) {
            appBarLayout1.visibility = View.VISIBLE
            appBarLayout2.visibility = View.GONE
        } else {
            appBarLayout1.visibility = View.GONE
            appBarLayout2.visibility = View.VISIBLE
        }


        rv_post_person.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post_person.setBackgroundResource(R.color.background_grey_little)
        rv_post_person.isNestedScrollingEnabled = false

        Glide.with(this)
            .load(viewModel.userPhotoUrl.value ?: sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_user_thumbnail))
            .into(img_avatar)
        Glide.with(this)
            .load(viewModel.userPhotoUrl.value ?: sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_user_thumbnail))
            .into(img_avatar_little)

        btn_add_story.setOnClickListener {
            val intent =
                Intent(this@PersonalProfileFragment.context, PickImageStoryActivity::class.java)
            startActivity(intent)
        }

        btn_edit_profile.setOnClickListener {
            val intent =
                Intent(this@PersonalProfileFragment.context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        ln_thinking_post.setOnClickListener {
            val intent =
                Intent(this@PersonalProfileFragment.context, CreatePostsActivity::class.java)
            startActivity(intent)
        }

        im_back.setOnClickListener {
            activity?.onBackPressed()
        }

        imgChangeAvatar.setOnClickListener {
            startForResult.launch(
                Intent(
                    this@PersonalProfileFragment.context,
                    PickImageResultActivity::class.java
                )
            )
        }

        imgSetting.setOnClickListener {
            transitFragment(SettingFragment(), R.id.container)
        }

    }

    private fun uploadFile(fileName: String) {
        val file = Uri.fromFile(File(fileName))

        val ref = FirebaseUtils.storageRef.child("image_user/${System.currentTimeMillis()}")

        val uploadTask = ref.putFile(file)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    showSnackBar(it.message!!)
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                FirebaseUtils.db.collection("User").document(viewModel.idUser.value!!).update(
                    mapOf(
                        "photoUrl" to downloadUri
                    )
                )
            } else {

            }
        }

        try {
            // function throw ra exception
        } catch (e: IOException) {

        }
    }


}