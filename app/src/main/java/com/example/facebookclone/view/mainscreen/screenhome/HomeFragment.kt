package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.facebookclone.R
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.facebookclone.model.*
import com.example.facebookclone.utils.*
import com.example.facebookclone.view.adapter.OptionsHomeAdapter
import com.example.facebookclone.view.adapter.PostAdapter
import com.example.facebookclone.view.adapter.StoryViewAdapter
import com.example.facebookclone.view.mainscreen.storyviewext.OnStoryChangedCallback
import com.example.facebookclone.view.mainscreen.storyviewext.StoryClickListeners
import com.example.facebookclone.view.mainscreen.storyviewext.StoryView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.img_avatar


class HomeFragment : Fragment() {
    private lateinit var databasestory: DatabaseReference
    private lateinit var databasepost: DatabaseReference
    private var optionsAdapter: OptionsHomeAdapter? = null
    private var storyAdapter: StoryViewAdapter? = null
    private var postAdapter: PostAdapter? = null
    private var id: String = ""
    private var user: String = ""
    private var idpostget: Long = 0L

    //    private val storyAdapter by lazy {
//        StoryViewAdapter(urlAvatar,requireContext(), list = listStory()) { story->
//            val intent = Intent(this@HomeFragment.context,PickImageStoryActivity::class.java)
//            startActivity(intent)
//        }
//    }
    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvatar: String = ""
    private var userName: String = ""
    private var idUser: String = ""
    private val listStory = mutableListOf<Any>()
    private val listPost = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databasestory = Firebase.database.reference.child("story")
        databasepost = Firebase.database.reference.child("posts")
        sharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        urlAvatar = sharedPreferences.getString(URL_PHOTO, "").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        idUser = sharedPreferences.getString(USER_ID, "USER_ID").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {

        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fb_avatar))
            .into(img_avatar)
        tv_thinking_home.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(USER_NAME, userName)
            bundle.putSerializable(URL_PHOTO, urlAvatar)
            val intent = Intent(this@HomeFragment.context, CreatePostsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }


//        img_avatar.setOnClickListener {
//
//        }
        initRecyclerView()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView() {
        optionsAdapter =
            OptionsHomeAdapter(requireContext(), listOptions = listOption()) { optionsHome ->

            }

        rv_home.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = optionsAdapter

        storyAdapter = StoryViewAdapter(urlAvatar, requireContext(), listStory) { objectStory ->
            id = objectStory.idUser
            showStories(id)
        }
        rv_story.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_story.setHasFixedSize(true)
        rv_story.adapter = storyAdapter

        listStory.add(ObjectStory("0", "0"))
        getAllStory()



        postAdapter = PostAdapter(idUser, requireContext(), listPost, { post ->
//            idpostget = post.idPost
        }, { reaction, postSelect ->
            Log.d("nht", "onReactionChange: " + reaction.reactText)
//            databasepost.child("idPost").equalTo(postSelect.idPost.toString()).get().addOnSuccessListener {
//                Log.i("firebase", "Got value ${it.value}")
//                if(post != null){
//                    if(post.listLike !is List<ListLike>){
//                        post.listLike.add(ListLike("",TypeLike.NO))
//                    }
//                    val idpost = post.idPost.toString()
//                    val listLike: MutableList<ListLike> = post!!.listLike
//                    if(reaction.reactText != null){
//                        val checkUser: ListLike? = listLike.stream()
//                            .filter { customer -> idUser == customer.idUser }
//                            .findAny()
//                            .orElse(null)
//
//                        if(checkUser == null){
//                            val newUser = ListLike()
//                            newUser.idUser = idUser
//                            newUser.srcLike = typeReaction(reaction.reactText)
//                            listLike.add(newUser)
//                        }
//                        databasepost.child(idpost).setValue(listLike)
//                            .addOnSuccessListener {
//                                Log.d("nht", "initView: success")
//                                // Write was successful!
//                                // ...
//                            }
//                            .addOnFailureListener {
//                                // Write failed
//                                // ...
//                            }
//
//                    }
//                }
//            }.addOnFailureListener{
//                Log.e("firebase", "Error getting data", it)
//            }

            databasepost?.child(postSelect.idPost.toString())
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        // handle error
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val listLike = dataSnapshot.getValue<Post>()?.listLike!!

                        if (listLike.isEmpty()) {
                            listLike.add(ListLike(idUser, TypeLike.LIKE))
                        } else {
                            val check = listLike.stream().filter { item -> idUser == item.idUser }
                                .findAny()
                                .orElse(null)
                            if (check == null) {
                                listLike.add(ListLike(idUser, TypeLike.LIKE))
                            } else {
                                listLike.remove(check)
                            }
                        }

                        postSelect.listLike = listLike

                        val mutableMap: MutableMap<String, Any> = mutableMapOf()
                        mutableMap[postSelect.idPost.toString()] = postSelect
                        databasepost.updateChildren(mutableMap)
                    }
                })

        })
        rv_post.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post.setHasFixedSize(true)
        rv_post.setBackgroundResource(R.color.background_grey_little)
        rv_post.isNestedScrollingEnabled = false
        rv_post.adapter = postAdapter
        getAllPosts()

    }

    private fun listOption(): MutableList<OptionsHome> {
        val listOption = mutableListOf<OptionsHome>()
        listOption.add(
            OptionsHome(
                optionName = "Reels",
                srcImage = R.drawable.img_options_reel,
                backgroundColor = R.color.color_reel
            )
        )
        listOption.add(
            OptionsHome(
                optionName = "Room",
                srcImage = R.drawable.img_options_room,
                backgroundColor = R.color.color_room
            )
        )
        listOption.add(
            OptionsHome(
                optionName = "Group",
                srcImage = R.drawable.img_options_group,
                backgroundColor = R.color.color_group
            )
        )
        listOption.add(
            OptionsHome(
                optionName = "Live",
                srcImage = R.drawable.img_options_live,
                backgroundColor = R.color.color_live
            )
        )
        return listOption
    }

//    private fun listPost(): MutableList<Post> {
//        val listPost = mutableListOf<Post>()
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//        listPost.add(Post(1,"Hoang Anh",mutableListOf<String>(),TypeFile.IMAGE,0,0,0,"0","0"));
//
//        return listPost
//    }

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
                // Get Post object and use the values to update the UI
                Log.d("hunghkp1", "onDataChange: ${dataSnapshot.toString()}")
                for (postSnapshot in dataSnapshot.children) {

//                    if (postSnapshot.value is Post){
                    Log.d("hunghkp1", "onDataChange1: ${postSnapshot.toString()}")
                    val post = postSnapshot.getValue<Post>()

                    Log.d("hunghkp1", "onDataChange2: ${post.toString()}")
                    if (post != null) {
                        if (post.listLike !is List<ListLike>) {
                            post.listLike.add(ListLike("", TypeLike.NO))

                        }
                        if (checkPost(post.idPost, listPost) == 1) {
                            listPost.add(post)
                        }
//                        }
                    }
                }
                Log.d("hunghkp", "${listPost.size} ")
                postAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("nht", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databasepost.addValueEventListener(postListener)

    }

    fun checkPost(a: Long, b: MutableList<Any>): Int {
        val k = b as MutableList<Post>
        for (i in k) {
            if (a == i.idPost)
                return 0
        }
        return 1
    }

}


