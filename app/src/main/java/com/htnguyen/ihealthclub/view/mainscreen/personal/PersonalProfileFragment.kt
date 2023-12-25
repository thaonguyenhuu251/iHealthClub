package com.htnguyen.ihealthclub.view.mainscreen.personal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.base.BaseFragment
import com.htnguyen.ihealthclub.databinding.FragmentPersonalProfileBinding
import com.htnguyen.ihealthclub.model.TypeAction
import com.htnguyen.ihealthclub.model.UserAction
import com.htnguyen.ihealthclub.utils.*
import kotlinx.android.synthetic.main.fragment_personal_profile.*

class PersonalProfileFragment :
    BaseFragment<FragmentPersonalProfileBinding, PersonalProfileViewModel>() {
    private lateinit var sharedPreferences: SharedPreferences
    private var postAdapter: PostAdapter? = null
    private val viewModel by viewModels<PersonalProfileViewModel>()

    override val layout: Int
        get() = R.layout.fragment_personal_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
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
        rv_post_person.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post_person.setBackgroundResource(R.color.background_grey_little)
        rv_post_person.isNestedScrollingEnabled = false

        Glide.with(this).load(viewModel.userPhotoUrl.value ?: sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_user_thumbnail))
            .into(img_avatar)
        Glide.with(this).load(viewModel.userPhotoUrl.value ?: sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_user_thumbnail))
            .into(img_avatar_little)

        btn_add_story.setOnClickListener {
            val intent =
                Intent(this@PersonalProfileFragment.context, PickImageStoryActivity::class.java)
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

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PersonalProfileFragment()
    }
}