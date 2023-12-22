package com.htnguyen.ihealthclub.view.mainscreen.personal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.Post
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.URL_PHOTO
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.utils.USER_NAME
import com.htnguyen.ihealthclub.view.adapter.PostAdapter
import com.htnguyen.ihealthclub.view.mainscreen.home.BottomSheetCommentFragment
import com.htnguyen.ihealthclub.view.mainscreen.home.CreatePostsActivity
import com.htnguyen.ihealthclub.view.mainscreen.home.PickImageStoryActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.base.BaseFragment
import com.htnguyen.ihealthclub.databinding.FragmentPersonalProfileBinding
import com.htnguyen.ihealthclub.view.register.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_personal_profile.*
import kotlinx.android.synthetic.main.fragment_personal_profile.img_avatar

class PersonalProfileFragment :
    BaseFragment<FragmentPersonalProfileBinding, PersonalProfileViewModel>() {
    private lateinit var databasepost: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private var postAdapter: PostAdapter? = null
    private val listPost = mutableListOf<Any>()
    private lateinit var btsComment: BottomSheetCommentFragment
    private val viewModel by viewModels<PersonalProfileViewModel>()

    override val layout: Int
        get() = R.layout.fragment_personal_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databasepost = Firebase.database.reference.child("posts")
        sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        initData()
        initView()

    }

    private fun initData() {
        binding.viewModel = viewModel
        viewModel.idUser.value =
            sharedPreferences.getString(USER_ID, "").toString()

        viewModel.getDataProfileUser()
    }

    private fun initView() {
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
        initRecycleView()

        im_back.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun initRecycleView() {
        /*postAdapter = PostAdapter(viewModel.idUser.value!!, requireContext(), listPost, { post ->

            }, { reaction, post ->

            }, { linear, post ->
                btsComment = BottomSheetCommentFragment.newInstance(post.idPost)
                btsComment.show(childFragmentManager, "")
            })
        rv_post_person.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post_person.setHasFixedSize(true)
        rv_post_person.setBackgroundResource(R.color.background_grey_little)
        rv_post_person.isNestedScrollingEnabled = false
        rv_post_person.adapter = postAdapter*/
        //getAllPosts()
    }

    /*private fun getAllPosts() {
        val postListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<Post>()
                    if (post != null) {
                        if (post.idUser == sharedPreferences.getString(USER_ID, "").toString()&& checkPost(
                                post.idPost,
                                listPost
                            ) == 1
                        ) {
                            listPost.add(post)
                        }
                    }

                }
                Log.d("ThaoNH", "${listPost.size} ")
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databasepost.addValueEventListener(postListener)

    }*/

    /*fun checkPost(a: Long, b: MutableList<Any>): Int {
        val k = b as MutableList<Post>
        for (i in k) {
            if (a == i.idPost)
                return 0
        }
        return 1
    }*/

    companion object {
        @JvmStatic
        fun newInstance() =
            PersonalProfileFragment()
    }
}