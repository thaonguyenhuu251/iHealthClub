package com.example.facebookclone.view.mainscreen.screenhome

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.utils.URL_PHOTO
import com.example.facebookclone.utils.USER_NAME
import com.example.facebookclone.view.dialog.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_create_post.container
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_bottom_create_post.*
import kotlinx.android.synthetic.main.layout_menu_bottom_create_post.*
import java.io.File
import java.io.IOException
import java.lang.Exception


class CreatePostsActivity : AppCompatActivity() {
    val storage = Firebase.storage
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private val storageRef = Firebase.storage.reference
    private val listDownloadUri = mutableListOf<String>()
    private var loadingDialog: LoadingDialog? = null

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val data = intent?.getStringExtra("KEY_PATH_IMAGE")

            Glide.with(this).load(data).into(img_pick)
            img_pick.visibility = View.VISIBLE
            ln_add_images.visibility = View.VISIBLE
            et_thinking_pos.isCursorVisible = true
            card_menu.visibility = View.GONE
            ln_options_post_home.visibility = View.VISIBLE
            val filePath = getRealPathFromUri(this,Uri.parse(data))
            uploadFile(filePath!!)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        initView()
    }


    @SuppressLint("ResourceAsColor")
    private fun initView(){
        atv_post.text = USER_NAME
        //img_avatar
        ln_object_post.setOnClickListener {
            val intent =Intent(this,ObjectPostHomeActivity::class.java)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

        im_post_more.setOnClickListener {
            card_menu.visibility = View.VISIBLE
            ln_options_post_home.visibility = View.GONE
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            hideSoftKeyboard(this,container)
        }

        iv_images.setOnClickListener {
            startForResult.launch(Intent(this, PickImagePostActivity::class.java))
        }

        ln_images.setOnClickListener {
            startForResult.launch(Intent(this, PickImagePostActivity::class.java))
        }

        iv_emoji.setOnClickListener {
            val intent =Intent(this,EmojiActionActivity::class.java)
            startActivity(intent)
        }

        et_thinking_pos.setOnClickListener {
            et_thinking_pos.isFocusable = true
        }

        et_thinking_pos.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
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
                if(et_thinking_pos.text.toString().trim().isNotEmpty()){
                    btn_post.isEnabled = true
                    btn_post.setBackgroundResource(R.drawable.rounded_button_posts)
                    btn_post.setTextColor(R.color.black_mode)
                }
                else{
                    if(btn_post.isEnabled){
                        btn_post.isEnabled = false
                        btn_post.setBackgroundResource(R.drawable.rounded_home_post_file)
                        btn_post.setTextColor(R.color.general_grey)
                    }

                }
            }
        })

        btn_post.setOnClickListener {
        }


        initBottomSheet()
    }


    private fun initBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(card_menu)

        bottomSheetBehavior?.peekHeight = 200

        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

    private fun uploadFile(fileName : String){
        //loading
        pb_image.visibility =  View.VISIBLE
        val file = Uri.fromFile(File(fileName))

        val ref = storageRef.child("posts")

        val uploadTask = ref.putFile(file)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    pb_image.visibility =  View.GONE
                    showSnackBar(it.message!!)

                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                listDownloadUri.add(downloadUri.toString())
                pb_image.visibility =  View.GONE
            } else {
                // Handle failures
                // ...

            }
        }

        try{
            // function throw ra exception
        }catch (e : IOException){

        }
    }


    private fun hideSoftKeyboard(activity: Activity, view: View?) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun View.showKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
}

