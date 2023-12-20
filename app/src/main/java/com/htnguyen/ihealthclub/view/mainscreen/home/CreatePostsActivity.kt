package com.htnguyen.ihealthclub.view.mainscreen.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.*
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_create_post.container
import kotlinx.android.synthetic.main.layout_bottom_create_post.*
import kotlinx.android.synthetic.main.layout_menu_bottom_create_post.*
import java.io.File
import java.io.IOException


class CreatePostsActivity : AppCompatActivity() {
    val storage = Firebase.storage
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private val storageRef = Firebase.storage.reference
    private val listDownloadUri = mutableListOf<String>()
    private val listLike = mutableListOf<ListLike>()
    private var loadingDialog: LoadingDialog? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var post: Post
    private lateinit var database: DatabaseReference
    private  var userName: String = ""
    private  var status: String = ""
    private var urlAvartar: String = ""
    private  var thinking: String = ""
    private lateinit var typeFile: TypeFile
    private  var emojiHome: EmojiHome?= null

    @SuppressLint("ResourceAsColor")
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getStringExtra(KEY_PATH_IMAGE_POST)

                Glide.with(this).load(data).into(img_pick)
                img_pick.visibility = View.VISIBLE
                ln_add_images.visibility = View.VISIBLE
                et_thinking_pos.isCursorVisible = true
                card_menu.visibility = View.GONE
                ln_options_post_home.visibility = View.VISIBLE

                btn_post.isEnabled = true
                btn_post.setBackgroundResource(R.drawable.rounded_button_little_blue)
                btn_post.setTextColor(R.color.black_mode)

                val filePath = getRealPathFromUri(this, Uri.parse(data))
                uploadFile(filePath!!)
            }
        }

    @SuppressLint("ResourceAsColor")
    private val startActivityPickEmoji =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val data = intent?.getSerializableExtra(KEY_EMOJI_PUT) as EmojiHome
                emojiHome = data
                Log.d("hunghkp", ": ${data.emojiName}")
                status = " ${data.srcImage} felling ${data.emojiName}"
//                val st =  data.srcImage + "felling ${data.emojiName}"
                atv_post.text = userName + status
                btn_post.isEnabled = true
                btn_post.setBackgroundResource(R.drawable.rounded_button_little_blue)
                btn_post.setTextColor(R.color.black_mode)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        database = Firebase.database.reference
        urlAvartar = sharedPreferences.getString(URL_PHOTO,"").toString()
        typeFile = TypeFile.OTHER
        post = Post()
        initView()
    }


    @SuppressLint("ResourceAsColor")
    private fun initView() {
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        atv_post.text = userName

        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(this, R.drawable.ic_user_thumbnail)).into(img_avatar)



        ln_object_post.setOnClickListener {
            val intent = Intent(this, ObjectPostHomeActivity::class.java)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

        im_post_more.setOnClickListener {
            card_menu.visibility = View.VISIBLE
            ln_options_post_home.visibility = View.GONE
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            hideSoftKeyboard(this, container)
        }

        iv_images.setOnClickListener {
            startForResult.launch(Intent(this, PickImagePostActivity::class.java))
        }

        ln_images.setOnClickListener {
            startForResult.launch(Intent(this, PickImagePostActivity::class.java))
        }

        iv_emoji.setOnClickListener {

            val intent = Intent(this, EmojiActionActivity::class.java)
            if(emojiHome != null){
                intent.putExtra(KEY_EMOJI_PUT, emojiHome)
            }

            startActivityPickEmoji.launch(intent)
        }
        ln_emoji.setOnClickListener {
//            val intent = Intent(this, EmojiActionActivity::class.java)
//            startActivity(intent)
            val intent = Intent(this, EmojiActionActivity::class.java)
            if(emojiHome != null){
                intent.putExtra(KEY_EMOJI_PUT, emojiHome)
            }

            startActivityPickEmoji.launch(intent)
        }

        et_thinking_pos.setOnClickListener {
            et_thinking_pos.isFocusable = true
        }

        et_thinking_pos.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                et_thinking_pos.isCursorVisible = true
                card_menu.visibility = View.GONE
                ln_options_post_home.visibility = View.VISIBLE
            }
        }


        et_thinking_pos.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (et_thinking_pos.text.toString().trim().isNotEmpty()) {
                    btn_post.isEnabled = true
                    btn_post.setBackgroundResource(R.drawable.rounded_button_little_blue)
                    btn_post.setTextColor(R.color.black_mode)
                    thinking = et_thinking_pos.text.toString().trim()

                } else {
                    if (btn_post.isEnabled) {
                        btn_post.isEnabled = false
                        btn_post.setBackgroundResource(R.drawable.rounded_home_post_file)
                        btn_post.setTextColor(R.color.general_grey)
                    }

                }

            }
        })

        btn_post.setOnClickListener {
            listLike.add(ListLike("", TypeLike.NO))
            post.idPost = System.currentTimeMillis()
            post.idUser = sharedPreferences.getString(USER_ID, "").toString()
            post.urlAvatar = urlAvartar
            post.status = thinking
            post.emojiStatus = status
            post.listFile = listDownloadUri
            post.typeFile = typeFile
            post.listLike = listLike
            post.likeTotal = 0
            post.commentTotal = 0
            post.shareTotal = 0
            post.createAt = System.currentTimeMillis()
            post.createBy = sharedPreferences.getString(USER_NAME, "").toString()
            database.child("posts").child(post.idPost.toString()).setValue(post)
                .addOnSuccessListener {
                    finish()
                    Log.d("hunghkp", "initView: success")
                }.addOnFailureListener { e ->
                Log.d("hunghkp", "initView: ${e.message}")
                    finish()
            }

        }


        initBottomSheet()
    }


    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(card_menu)

        bottomSheetBehavior?.peekHeight = 200

        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        card_menu.visibility = View.GONE
                        ln_options_post_home.visibility = View.VISIBLE
                    }
                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun uploadFile(fileName: String) {
        //loading
        pb_image.visibility = View.VISIBLE
        val file = Uri.fromFile(File(fileName))

        val ref = storageRef.child("post/${System.currentTimeMillis()}")

        val uploadTask = ref.putFile(file)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    pb_image.visibility = View.GONE
                    showSnackBar(it.message!!)
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                listDownloadUri.add(downloadUri.toString())
                pb_image.visibility = View.GONE
                typeFile = TypeFile.IMAGE
            } else {
                // Handle failures
                // ...

            }
        }

        try {
            // function throw ra exception
        } catch (e: IOException) {

        }
    }


    private fun hideSoftKeyboard(activity: Activity, view: View?) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun View.showKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
}

