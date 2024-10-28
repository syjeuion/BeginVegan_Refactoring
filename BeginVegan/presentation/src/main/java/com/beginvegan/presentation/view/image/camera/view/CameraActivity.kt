package com.beginvegan.presentation.view.image.camera.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseActivity
import com.beginvegan.presentation.databinding.ActivityCameraBinding
import com.beginvegan.presentation.util.DefaultDialog
import com.beginvegan.presentation.view.image.camera.viewModel.CameraViewModel
import com.beginvegan.presentation.view.image.gallery.model.GalleryImage
import com.takusemba.cropme.OnCropListener
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Throws

class CameraActivity : BaseActivity<ActivityCameraBinding>(R.layout.activity_camera) {

    private val viewModel: CameraViewModel by viewModels()

    private val cameraPermission = Manifest.permission.CAMERA

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>


    private lateinit var imageAbsolutePath: String
    private var imageUri: Uri? = null
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            showToast("$isGranted")
            if (isGranted) {
                showToast("registerForActivityResult:권한승인")
                openCamera()
            } else {
                logMessage(
                    "requestPermissionLauncher 런처 = ${
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                        )
                    } "
                )
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                ) {
                    showPermissionDeniedDialog()
                } else {

                    showPermissionRationaleDialog()
                }
//                showToast("registerForActivityResult:권한")
//                showPermissionRationaleDialog(this)
            }
        }

    override fun initViewModel() {
    }

    override fun init() {
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                logMessage("Success Image Capture")
                binding.clCropper.setUri(imageUri!!)
            } else {
                logMessage("Failed or Cancel Image Capture")
                finish()
            }
        }
        checkPermissionCamera()


        binding.ibBackUp.setOnClickListener {
            openCamera()
        }
        binding.ibDone.setOnClickListener {
            binding.clCropper.crop()
        }
        binding.clCropper.addOnCropListener(object : OnCropListener {
            override fun onFailure(e: Exception) {
                Timber.d("사진 크롭 실패")
            }

            override fun onSuccess(bitmap: Bitmap) {
                val path = bitmapToFilePath(bitmap)
                Timber.d(
                    "사진 크롭 성공\n" +
                            "$bitmap\n" +
                            "$path\n"
                )
                intent.putExtra("IMAGE_DATA", GalleryImage(Uri.fromFile(File(path)), false, path))
                setResult(RESULT_OK, intent)
                finish()
            }

        })


    }

    private fun bitmapToFilePath(inImage: Bitmap): String? {
        val imageFile =
            File(this.cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg")
        imageFile.createNewFile()
        val bos = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(imageFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return imageFile.absolutePath
    }


    private fun checkPermissionCamera() {
        if (ActivityCompat.checkSelfPermission(
                this,
                PERMISSION_CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            logMessage("Camera Permission Granted")
            openCamera()
        } else {
            logMessage(
                "checkpermission 런처 = ${
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.CAMERA
                    )
                } "
            )
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
                showPermissionDeniedDialog()
            } else {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            try {
                logMessage("cameraIntent.resolveActivity(packageManager) not null")
                createCacheImageFile()?.let { photoFile ->
                    imageUri = FileProvider.getUriForFile(
                        this,
                        "com.example.beginvegan.fileprovider",
                        photoFile
                    )
                }
                logMessage("imageUri = $imageUri")
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                cameraLauncher.launch(cameraIntent)
            } catch (e: IOException) {

            }
        } else {
            logMessage("open camera error")
        }
    }

    @Throws(IOException::class)
    private fun createCacheImageFile(): File? {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", // 파일 이름의 접두사
            ".jpg",               // 파일 확장자
            directory              // 파일을 생성할 디렉토리
        ).also {
            logMessage("AbsolutePath = ${it.absolutePath}")
            imageAbsolutePath = it.absolutePath
        }
    }


    //     권한 재요청
    private fun showPermissionRationaleDialog() {
        var isRetry = false
        val dialog = DefaultDialog.Builder()
            .setTitle("권한 재요청 안내")
            .setBody(
                "해당 권한을 거부할 경우, 다음 기능의 사용이 불가능해요." +
                        "\n · Map 리뷰 작성 시, 이미지 등록 " +
                        "\n · Mypage 프로필 이미지 등록"
            )
            .setPositiveButton("권한재요청") {
                isRetry = true
                requestPermissionLauncher.launch(cameraPermission)
            }.setNegativeButton("닫기") {
                logMessage("닫기")
            }
            .setOnDismissListener {
                if (!isRetry) {
                    showPermissionDeniedDialog()
                }
            }.show(supportFragmentManager, "showPermissionRationaleDialog")
    }

    // 권한 허용 안함
    private fun showPermissionDeniedDialog() {
        val dialog = DefaultDialog.Builder()
            .setTitle("기능 사용 불가 안내")
            .setBody(
                "카메라 사용에 대한 권한 사용을 거부하셨어요. \n" +
                        "\n" +
                        "기능 사용을 원하실 경우 [휴대폰 설정 > 애플리케이션 관리자]에서 해당 앱의 권한을 허용해 주세요."
            ).setPositiveButton("확인") {
                logMessage("확인")
                this@CameraActivity.finish()
            }.show(supportFragmentManager, "showPermissionDeniedDialog")
    }

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }
}