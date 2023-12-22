package com.htnguyen.ihealthclub.view.mainscreen.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
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
    private lateinit var databasestory: DatabaseReference
    private lateinit var databasepost: DatabaseReference
    private var storyAdapter: StoryViewAdapter? = null
    private var postAdapter: PostAdapter? = null
    private var id: String = ""
    private var user: String = ""

    private lateinit var btsComment: BottomSheetCommentFragment

    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvatar: String = ""
    private var userName: String = ""
    private var idUser: String = ""
    private val listStory = mutableListOf<Any>()
    private val listPost = arrayListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databasestory = Firebase.database.reference.child("story")
        databasepost = Firebase.database.reference.child("Posts")
        sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        urlAvatar = sharedPreferences.getString(URL_PHOTO, "").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        idUser = sharedPreferences.getString(USER_ID, "USER_ID").toString()
    }

    override val layout: Int
        get() = R.layout.fragment_home

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
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
            transitFragment(PersonalProfileFragment(), R.id.frameContainer)
        }

        initRecyclerView()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        storyAdapter = StoryViewAdapter(urlAvatar, requireContext(), listStory) { objectStory ->
            id = objectStory.idUser
            showStories(id)
        }
        rv_story.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_story.setHasFixedSize(true)
        rv_story.adapter = storyAdapter
        rv_story.isNestedScrollingEnabled = false
        listStory.add(ObjectStory("0", "0"))
        getAllStory()

        postAdapter = PostAdapter(idUser, requireContext(), listPost, { post ->

        }, { reaction, postSelect ->
            val userReactionLike = UserAction(
                idUser = idUser,
                typeAction = reaction.reactTypeAction,
                timeAction = System.currentTimeMillis(),
                contentAction = ""
            )
            databasepost.child(postSelect.idPost).child("listLike").child(idUser)
                .setValue(userReactionLike)
        }, { linear, post ->
            btsComment = BottomSheetCommentFragment.newInstance(post.idPost)
            btsComment.show(childFragmentManager, "")

        })
        rv_post.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post.setHasFixedSize(true)
        rv_post.setBackgroundResource(R.color.background_grey_little)
        rv_post.isNestedScrollingEnabled = false
        rv_post.adapter = postAdapter
        getAllPosts()

    }

    fun checkStory(a: String, b: MutableList<Any>): Int {
        val k = b as MutableList<ObjectStory>
        for (i in k) {
            if (a == i.idUser)
                return 0
        }
        return 1
    }

    private fun getAllStory() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (postSnapshot in dataSnapshot.children) {
                    val story = postSnapshot.getValue<ObjectStory>()
                    if (story != null) {
                        if (checkStory(story.idUser, listStory) == 1) {
                            listStory.add(story)
                        }

                    }
                }

                Log.d("hunghkp", "${listStory.size} ")
                storyAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("nht", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databasestory.addValueEventListener(postListener)

    }

    fun showStories(idCreate: String) {
        val myStories: ArrayList<ObjectStory> = ArrayList<ObjectStory>()

        databasestory.orderByChild("idUser").equalTo(idCreate)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val story = postSnapshot.getValue<ObjectStory>()
                        if (story != null) {
                            myStories.add(story)
                            user = story.createBy
                        }
                    }

                    StoryView.Builder(childFragmentManager)
                        .setStoriesList(myStories)
                        .setStoryDuration(5000)
                        .setTitleText(user)
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

    private fun getAllPosts() {
        val postListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val idPost = postSnapshot.child("idPost").getValue(String::class.java)!!
                    val idUser = postSnapshot.child("idUser").getValue(String::class.java)!!
                    val emojiStatus = postSnapshot.child("emojiStatus").getValue(String::class.java)!!
                    val bodyStatus = postSnapshot.child("bodyStatus").getValue(String::class.java)!!
                    val createAt = postSnapshot.child("createAt").getValue(Long::class.java)!!
                    val typePost = postSnapshot.child("typePost").getValue(TypeFile::class.java)!!
                    val likeTotal = postSnapshot.child("likeTotal").getValue(Int::class.java)!!
                    val commentTotal = postSnapshot.child("commentTotal").getValue(Int::class.java)!!
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
                            listFile,
                            listLike,
                        )
                    )
                }

                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databasepost.addValueEventListener(postListener)
    }

}


