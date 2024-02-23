package com.htnguyen.ihealthclub.view.mainscreen.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.BR
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseActivity
import com.htnguyen.ihealthclub.databinding.ActivityPickImageStoryBinding
import com.htnguyen.ihealthclub.model.GalleryPicture
import com.htnguyen.ihealthclub.utils.KEY_PATH_IMAGE_STORY
import com.htnguyen.ihealthclub.view.adapter.GalleryPicturesAdapter
import com.htnguyen.ihealthclub.view.mainscreen.photoeditor.EditImageActivity
import kotlinx.android.synthetic.main.activity_pick_image_story.*
import kotlinx.android.synthetic.main.activity_pick_image_story.iv_back



class PickImageStoryActivity : BaseActivity<ActivityPickImageStoryBinding, GalleryViewModel>() {
    private val adapter by lazy {
        GalleryPicturesAdapter(pictures, 10, this)
    }

    override val viewModel: GalleryViewModel by viewModels()

    private val pictures by lazy {
        ArrayList<GalleryPicture>(viewModel.getGallerySize(this))
    }
    override val layout: Int = R.layout.activity_pick_image_story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_image_story)
        if (checkPermissionImage()) {
            initView()
        } else {
            requestReadExternalStoragePermission()
        }

    }

    override fun getBindingVariable(): Int = BR.viewModel

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
                im_post_more.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                onClick = 1
            }
            else{
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
        viewModel.getImagesFromGallery(this, pageSize) {
            if (it.isNotEmpty()) {
                pictures.addAll(it)
                adapter.notifyItemRangeInserted(pictures.size, it.size)
            }
        }
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
            requestReadExternalStoragePermission()
        }
    }

}