package com.example.facebookclone.view.mainscreen.personal

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.model.Post
import com.example.facebookclone.utils.SHARED_PREFERENCES_KEY
import com.example.facebookclone.utils.URL_PHOTO
import com.example.facebookclone.utils.USER_ID
import com.example.facebookclone.utils.USER_NAME
import com.example.facebookclone.view.adapter.PostAdapter
import com.example.facebookclone.view.mainscreen.home.CreatePostsActivity
import com.example.facebookclone.view.mainscreen.home.PickImageStoryActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_personal_profile.*
import kotlinx.android.synthetic.main.fragment_personal_profile.img_avatar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databasepost: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvatar: String = ""
    private var userName: String = ""
    private var idUser: String = ""
    private var postAdapter: PostAdapter? = null
    private val listPost = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        databasepost = Firebase.database.reference.child("posts")
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        urlAvatar = sharedPreferences.getString(URL_PHOTO,"").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        idUser = sharedPreferences.getString(USER_ID,"USER ID").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView(){

        tv_user_name.text = userName

        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fb_avatar)).into(img_avatar)
        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fb_avatar)).into(img_avatar_little)

        btn_add_story.setOnClickListener{
            val intent = Intent(this@PersonalProfileFragment.context,PickImageStoryActivity::class.java)
            startActivity(intent)
        }

        ln_thinking_post.setOnClickListener{
            val intent = Intent(this@PersonalProfileFragment.context,CreatePostsActivity::class.java)
            startActivity(intent)
        }
        initRecycleView()

    }

    private fun initRecycleView(){
        postAdapter = PostAdapter(urlAvatar, requireContext(), listPost,{post->

        },{reaction, post ->

        },{
            linear, post ->
        })
        rv_post_person.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_post_person.setHasFixedSize(true)
        rv_post_person.setBackgroundResource(R.color.background_grey_little)
        rv_post_person.isNestedScrollingEnabled = false
        rv_post_person.adapter = postAdapter
        getAllPosts()
    }

    private fun getAllPosts() {
        val postListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue<Post>()
                    if (post != null) {
                        if (post.idUser == idUser && checkPost(post.idPost,listPost)==1){
                            listPost.add(post)
                        }
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
        val  k = b as MutableList<Post>
        for(i in k){
            if (a == i.idPost)
                return 0
        }
        return 1
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonalProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PersonalProfileFragment()
    }
}