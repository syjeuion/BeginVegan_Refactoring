package com.beginvegan.presentation.view.image.gallery.view

import android.Manifest
import android.content.pm.PackageManager
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseActivity
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.beginvegan.presentation.databinding.ActivityGalleryBinding
import com.beginvegan.presentation.util.DefaultDialog
import com.beginvegan.presentation.view.image.gallery.viewModel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : BaseActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {

    private val viewModel: GalleryViewModel by viewModels()

    override fun initViewModel() {
        viewModel.resultImage.observe(this) {
            if (it != null) {
                intent.putExtra("IMAGE_DATA", viewModel.resultImage.value)
                logMessage("Activity Result Launcher ${viewModel.resultImage.value}")
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        viewModel.permissionState.observe(this) {
            if (it == false) {
                finish()
            }
        }
    }

    // 여러 권한을 배열로 요청
    private val galleryPermissions: Array<String> = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
            arrayOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_VISUAL_USER_SELECTED
            )
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            arrayOf(
                READ_MEDIA_IMAGES
            )
        }

        else -> {
            arrayOf(READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { isGranted ->
            var isReadImages: Boolean
            var isUserSelected = false
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                    isReadImages = isGranted[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                    isUserSelected =
                        isGranted[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] ?: false
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    isReadImages = isGranted[Manifest.permission.READ_MEDIA_IMAGES] ?: false
                }

                else -> {
                    isReadImages = isGranted[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                handlePermissionUpsideDownCake(isReadImages, isUserSelected)
            } else {
                handlePermissionLegacy()
            }
        }

    override fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            logMessage("Build Version above Upside Down Cake")
            checkPermissionUpsideDownCake()
        } else {
            logMessage("Build Version below Tiramisu")
            checkPermissionLegacy()
        }
    }

    // Check self permission denial for Android 14 (UpsideDownCake) above.
    private fun checkPermissionUpsideDownCake() {
        val isSelfReadImages = ActivityCompat.checkSelfPermission(
            this,
            READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
        val isSelfUserSelected = ActivityCompat.checkSelfPermission(
            this,
            READ_MEDIA_VISUAL_USER_SELECTED
        ) == PackageManager.PERMISSION_GRANTED

        logMessage("| 셀프 권한 체크 | \n이미지 권한: $isSelfReadImages, \n유저 선택 권한: $isSelfUserSelected")

        if (isSelfReadImages && isSelfUserSelected) {
            // 모든 권한이 승인이 되어있는 상태 추가적인 요청 없음 , PermissionState true
            logMessage("모든 권한에 대해 승인")
        } else if (!isSelfReadImages && isSelfUserSelected) {
            // 유저 선택 이미지 상태는 초기값이 true
            logMessage("유저 선택 이미지만 승인 상태(Default)")
//            requestPermissionsLauncher.launch(galleryPermissions)
        } else if (!isSelfReadImages && !isSelfUserSelected) {
            // 모든 권한이 거부된 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                // 사용자 거부 경험 있음 - 더 이상 묻지않고, 권한 관리자를 통해 직접 변경하라는 다이얼로그 띄워주기
                logMessage(
                    "USER SELECTED =${
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        )
                    }\nIMAGES =${
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    }"
                )
                showPermissionDeniedDialog()
                logMessage("사용자 거부 경험 있음 ")
            } else {
                logMessage(
                    "USER SELECTED =${
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        )
                    }\nIMAGES =${
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                    }"
                )
                // 처음으로 거부 사용자 거부 경험 없음 && 처음 권한요청시 권한에 대한 모든 처리도 False
                logMessage("처음으로 거부 사용자 거부 경험 없음")
                requestPermissionsLauncher.launch(galleryPermissions)
            }
        } else {
            // 이론 상 존재하지 않으나 예외처리
            showToast("잘못된 요청입니다.")
            finish()
        }
    }

    // Check self permission denial for Android 13 below
    private fun checkPermissionLegacy() {
        val isSelfReadImages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
        val isRequestRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                READ_MEDIA_IMAGES
            )
        } else {
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                READ_EXTERNAL_STORAGE
            )
        }
        logMessage("handlePermissionLegacy")
        if (isSelfReadImages) {
            // 권한이 허용되어있는 상태
            viewModel.updatePermissionState(true)
        } else {
            // 권한이 허용되어있지 않은 상태
            if (isRequestRationale) {
                // 사용자가 거부 경험이 있는 상태 - 권한 허용 x
                showPermissionDeniedDialog()
            } else {
                // 사용자 거부 경험이 없는 상태
                // 권한 요청 런처 실행
                requestPermissionsLauncher.launch(galleryPermissions)
            }
        }
        viewModel.updatePermissionState(true)
    }

    // Handles permission denial for Android 14 (UpsideDownCake) and above.
    private fun handlePermissionUpsideDownCake(isReadImages: Boolean, isUserSelected: Boolean) {
        logMessage("handlePermissionUpsideDownCake")
        logMessage("이미지 권한: $isReadImages, \n유저 선택 권한: $isUserSelected")
        if (isReadImages && isUserSelected) {
            logMessage("모든 권한에 대해 승인")
        } else if (!isReadImages && isUserSelected) {
            logMessage("유저 선택 이미지만 승인")
        } else if (!isReadImages && !isUserSelected) {
            logMessage("사용자 거부 경험 없음")
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, READ_MEDIA_IMAGES)) {
                showPermissionDeniedDialog()
            } else {
                showPermissionRationaleDialog()
            }

        } else {
            showToast("잘못된 요청입니다.")
            finish()
        }
        viewModel.updatePermissionState(true)
    }

    // Handles permission denial for Android 13 and below
    private fun handlePermissionLegacy() {
        logMessage("handlePermissionLegacy")
        if (galleryPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }
        ) {
            showPermissionRationaleDialog()
        } else {
            showPermissionDeniedDialog()
        }
        viewModel.updatePermissionState(true)
    }

    // 권한 재요청
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
                requestPermissionsLauncher.launch(galleryPermissions)
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
                "저장소에 대한 권한 사용을 거부하셨어요.\n" +
                        "\n" +
                        "기능사용을 원하실 경우 [휴대폰 설정 > 애플리케이션 관리자]에서 해당 앱의 권한을 허용해 주세요."
            ).setPositiveButton("확인") {
                logMessage("확인")
                this@GalleryActivity.finish()
            }.show(supportFragmentManager, "showPermissionDeniedDialog")
    }

    companion object {
        private const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        private const val READ_MEDIA_VISUAL_USER_SELECTED =
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}
