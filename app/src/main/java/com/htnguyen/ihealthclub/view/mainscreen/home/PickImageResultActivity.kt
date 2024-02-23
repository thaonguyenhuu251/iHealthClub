package com.htnguyen.ihealthclub.view.mainscreen.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.htnguyen.ihealthclub.BR
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseActivity
import com.htnguyen.ihealthclub.databinding.ActivityPickImageResultBinding
import com.htnguyen.ihealthclub.model.GalleryPicture
import com.htnguyen.ihealthclub.utils.KEY_PATH_IMAGE_POST
import com.htnguyen.ihealthclub.view.adapter.GalleryPicturesAdapter
import kotlinx.android.synthetic.main.activity_pick_image_result.*

class PickImageResultActivity : BaseActivity<ActivityPickImageResultBinding, GalleryViewModel>() {

    private val adapter by lazy {
        GalleryPicturesAdapter(pictures, 10, this)
    }

    private val galleryViewModel: GalleryViewModel by viewModels()

    private val pictures by lazy {
        ArrayList<GalleryPicture>(galleryViewModel.getGallerySize(this))
    }
    override val layout: Int = R.layout.activity_pick_image_result
    override val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_image_result)

        if (checkPermissionImage()) {
            initImage()
        } else {
            requestReadExternalStoragePermission()
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    private fun initImage() {
        val layoutManager = GridLayoutManager(this, 3)
        val pageSize = 20
        var onClick = 0
        rv_images.layoutManager = layoutManager
        rv_images.addItemDecoration(SpaceItemDecoration(1))
        rv_images.adapter = adapter

        adapter.setOnClickListener { galleryPicture ->
            val resultIntent = Intent()

            resultIntent.putExtra(KEY_PATH_IMAGE_POST, galleryPicture.path)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        rv_images.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == pictures.lastIndex) {
                    loadPictures(pageSize)
                }
            }
        })

        iv_back.setOnClickListener {
            onBackPressed()
        }

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

        loadPictures(pageSize)
    }


    private fun loadPictures(pageSize: Int) {
        galleryViewModel.getImagesFromGallery(this, pageSize) {
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
            initImage()
        else {
            showToast("Permission Required to Fetch Gallery.")
            requestReadExternalStoragePermission()
        }
    }
}