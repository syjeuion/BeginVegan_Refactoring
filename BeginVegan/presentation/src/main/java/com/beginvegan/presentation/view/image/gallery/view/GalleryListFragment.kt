package com.beginvegan.presentation.view.image.gallery.view

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseFragment
import com.beginvegan.presentation.databinding.FragmentGalleryListBinding
import com.beginvegan.presentation.view.image.gallery.adpater.GalleryRVAdapter
import com.beginvegan.presentation.view.image.gallery.model.GalleryImage
import com.beginvegan.presentation.view.image.gallery.viewModel.GalleryViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class GalleryListFragment :
    BaseFragment<FragmentGalleryListBinding>(R.layout.fragment_gallery_list) {
    private val imageList = arrayListOf<Uri>()

    private val viewModel: GalleryViewModel by activityViewModels()

    private lateinit var navController: NavController
    private lateinit var galleryRVAdapter: GalleryRVAdapter
    override fun init() {
        navController = Navigation.findNavController(binding.root)
        viewModel.permissionState.observe(this) {
            // 갤러리 이미지 가져와서 뿌리기
            showGallery()
            setGalleryAdapter()
        }
        viewModel.imageList.observe(this) {
            Timber.d("Observe ImageList $imageList ")
        }
        binding.ibBackUp.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setGalleryAdapter() {
        galleryRVAdapter = GalleryRVAdapter()
        binding.rvGallery.adapter = galleryRVAdapter
        galleryRVAdapter.submitList(viewModel.imageList.value)

        galleryRVAdapter.setOnItemClickListener(object : GalleryRVAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: GalleryImage, position: Int) {
                if (data != null) {
                    viewModel.updateSelectImage(data)
                    navController.navigate(R.id.action_galleryListFragment_to_selectImageFragment)
                }
            }
        })
    }

    private fun getCursor(): Cursor? {
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.TITLE,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )
        return requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
        )
    }

    private fun showGallery() {
        lifecycleScope.launch {
            try {
                val cursor = getCursor()
                cursor?.use {
                    if (it.count == 0) {
                        Timber.d("No images found")
                    } else {
                        val images = mutableListOf<GalleryImage>()
                        val idColNum = it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                        val titleColNum =
                            it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)
                        val dateTakenColNum =
                            it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)

                        while (it.moveToNext()) {
                            val id = it.getLong(idColNum)
                            val title = it.getString(titleColNum)
                            val dateTaken = it.getLong(dateTakenColNum)
                            val imageUri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            images.add(GalleryImage(imageUri, false))
                            Timber.d("id: $id, title: $title, dateTaken: $dateTaken, imageUri: $imageUri")
                        }
                        Timber.d("fetch ImageList $imageList ")
                        viewModel.fetchGalleryImage(images)
                    }
                } ?: run {
                    Timber.d("Cursor is null")
                }
            } catch (e: Exception) {
                Timber.e(e, "에러: 스토리지 접근 권한을 허가해주세요")
            }
        }
    }
}