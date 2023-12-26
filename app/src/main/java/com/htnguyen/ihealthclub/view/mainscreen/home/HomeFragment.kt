package com.htnguyen.ihealthclub.view.mainscreen.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseFragment
import com.htnguyen.ihealthclub.databinding.FragmentHomeBinding
import com.htnguyen.ihealthclub.model.*
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.adapter.PostAdapter
import com.htnguyen.ihealthclub.view.adapter.StoryViewAdapter
import com.htnguyen.ihealthclub.view.mainscreen.personal.PersonalProfileFragment
import com.htnguyen.ihealthclub.view.mainscreen.storyviewext.OnStoryChangedCallback
import com.htnguyen.ihealthclub.view.mainscreen.storyviewext.StoryClickListeners
import com.htnguyen.ihealthclub.view.mainscreen.storyviewext.StoryView
import com.htnguyen.ihealthclub.view.register.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding, RegisterViewModel>() {
    private var urlAvatar: String = ""
    private var userName: String = ""
    private var idUser: String = ""

    private var postAdapter: PostAdapter? = null
    private var storyAdapter: StoryViewAdapter? = null


    override val layout: Int
        get() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlAvatar = sharedPreferences.getString(URL_PHOTO, "").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        idUser = sharedPreferences.getString(USER_ID, "USER_ID").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        rv_post.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post.setBackgroundResource(R.color.background_grey_little)
        rv_post.isNestedScrollingEnabled = false

        rv_story.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_story.setHasFixedSize(true)
        rv_story.isNestedScrollingEnabled = false

        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_user_thumbnail))
            .into(img_avatar)
        tv_thinking_home.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(USER_NAME, userName)
            bundle.putSerializable(URL_PHOTO, urlAvatar)
            val intent = Intent(this@HomeFragment.context, CreatePostsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        img_avatar.setOnClickListener {
            val args = Bundle()
            args.putInt(PERSON_TYPE, 0)
            transitFragment(PersonalProfileFragment(), R.id.frameContainer, args)
        }
    }

    override fun onStart() {
        super.onStart()
        val optionsPost: FirebaseRecyclerOptions<Post> =
            FirebaseRecyclerOptions.Builder<Post>()
                .setLifecycleOwner(this)
                .setQuery(FirebaseUtils.databasePost, Post::class.java)
                .build()
        postAdapter = PostAdapter(idUser, requireContext(), callback = {

        }, onActionLike = { reaction, postSelect ->
            val userReactionLike = UserAction(
                idUser = idUser,
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
        }, callback3 = { _, _ -> }, optionsPost)
        postAdapter?.startListening()
        rv_post.adapter = postAdapter

        val optionsStory: FirebaseRecyclerOptions<ObjectStory> =
            FirebaseRecyclerOptions.Builder<ObjectStory>()
                .setLifecycleOwner(this)
                .setQuery(FirebaseUtils.databaseStory, ObjectStory::class.java)
                .build()
        storyAdapter =
            StoryViewAdapter(urlAvatar, requireContext(), optionsStory, callback = { objectStory, userName ->
                showStories(objectStory.idUser, userName)
            })
        storyAdapter?.startListening()
        rv_story.adapter = storyAdapter
    }

    override fun onStop() {
        super.onStop()
        postAdapter?.stopListening()
        storyAdapter?.stopListening()
    }

    private fun showStories(idCreate: String, userName: String) {
        val myStories = ArrayList<ObjectStory>()
        FirebaseUtils.databaseStory.orderByChild("idUser").equalTo(idCreate)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val story = postSnapshot.getValue<ObjectStory>()
                        if (story != null) {
                            myStories.add(story)
                        }
                    }

                    StoryView.Builder(childFragmentManager)
                        .setStoriesList(myStories)
                        .setStoryDuration(5000)
                        .setTitleText(userName)
                        .setSubtitleText("Damascus")
                        .setStoryClickListeners(object : StoryClickListeners {
                            override fun onDescriptionClickListener(position: Int) {

                            }

                            override fun onTitleIconClickListener(position: Int) {}
                        })
                        .setOnStoryChangedCallback(object : OnStoryChangedCallback {
                            override fun storyChanged(position: Int) {

                            }

                        })
                        .setStartingIndex(0)
                        .setRtl(false)
                        .build()
                        .show()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }

            )
    }

}