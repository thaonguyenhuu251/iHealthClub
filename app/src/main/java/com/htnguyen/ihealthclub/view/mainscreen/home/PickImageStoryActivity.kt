package com.htnguyen.ihealthclub.view.mainscreen.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.GalleryPicture
import com.htnguyen.ihealthclub.utils.KEY_PATH_IMAGE_STORY
import com.htnguyen.ihealthclub.view.adapter.GalleryPicturesAdapter
import com.htnguyen.ihealthclub.view.mainscreen.photoeditor.EditImageActivity
import kotlinx.android.synthetic.main.activity_pick_image_story.*
import kotlinx.android.synthetic.main.activity_pick_image_story.iv_back



class PickImageStoryActivity : AppCompatActivity() {
    private val adapter by lazy {
        GalleryPicturesAdapter(pictures, 10, this)
    }

    private val galleryViewModel: GalleryViewModel by viewModels()

    private val pictures by lazy {
        ArrayList<GalleryPicture>(galleryViewModel.getGallerySize(this))
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_image_story)
        requestReadStoragePermission()

    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                readStorage
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else initView()
    }
    @SuppressLint("ResourceAsColor")
    private fun initView() {
        val layoutManager = GridLayoutManager(this, 3)
        val pageSize = 20
        var onClick = 0
        rv_images.layoutManager = layoutManager
        rv_images.addItemDecoration(SpaceItemDecoration(1))
        rv_images.adapter = adapter

        adapter.setOnClickListener {
            galleryPicture ->
            val resultIntent = Intent(this, EditImageActivity::class.java)
            resultIntent.putExtra(KEY_PATH_IMAGE_STORY, galleryPicture.path)
            startActivity(resultIntent)
            finish()
        }

        rv_images.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == pictures.lastIndex) {
                    loadPictures(pageSize)
                }
            }
        })

        ln_photo_library.setOnClickListener {
            if(onClick == 0){
                ln_photo_library.setBackgroundResource(R.drawable.rounded_home_post_file)
                im_post_more.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                onClick = 1
            }
            else{
                ln_photo_library.setBackgroundResource(R.drawable.rounded_not_background)
                im_post_more.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                onClick = 0
            }

        }

        iv_back.setOnClickListener {
            finish()
        }

        loadPictures(pageSize)
    }

    private fun loadPictures(pageSize: Int) {
        galleryViewModel.getImagesFromGallery(this, pageSize) {
            if (it.isNotEmpty()) {
                pictures.addAll(it)
                adapter.notifyItemRangeInserted(pictures.size, it.size)
            }
            Log.i("GalleryListSize", "${pictures.size}")
        }
//        galleryViewModel.getVideosFromGallery(this, pageSize) {
//            if (it.isNotEmpty()) {
//                pictures.addAll(it)
//                adapter.notifyItemRangeInserted(pictures.size, it.size)
//            }
//            Log.i("GalleryListSize", "${pictures.size}")
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initView()
        else {
            showToast("Permission Required to Fetch Gallery.")
            super.onBackPressed()
        }
    }

    private fun showToast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}