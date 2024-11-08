package com.beginvegan.presentation.view.main.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.beginvegan.core_fcm.model.FcmData
import com.beginvegan.presentation.R
import com.beginvegan.presentation.base.BaseActivity
import com.beginvegan.presentation.config.navigation.MainNavigationHandler
import com.beginvegan.presentation.config.navigation.MainNavigationImpl
import com.beginvegan.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    lateinit var mainNavigationHandler: MainNavigationHandler
    lateinit var navController: NavController
    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivityMainBinding.inflate(layoutInflater)
    override fun initViewModel() {}

    override fun init() {
        binding.dlDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setNavController()
        setBottomNav()
        setupOnBackPressedCallback()

        checkFcmType()
    }

    /**
     * FCM Push 알림 클릭 시 분기 처리
     */
    private fun checkFcmType(){
        val fcmData = intent.getParcelableExtra<FcmData>("fcmData")
        fcmData?.alarmType.let{
            Timber.d("fcmData?.alarmType.let: $it")
            when(it){
                "MYPAGE" -> mainNavigationHandler.navigateToMypage()
                "MAP" -> mainNavigationHandler.navigateToMap()
                "TIPS" -> mainNavigationHandler.navigateToTips()
                "INFORMATION" -> {}
            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcw_main_container) as NavHostFragment
        navController = navHostFragment.navController
        mainNavigationHandler = MainNavigationImpl(navController)
    }

    private fun setBottomNav() {
        with(binding) {
            bnvMain.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.bnvMain.visibility =
                    if(bottomNavVisibilityMap.contains(destination.id)) View.VISIBLE
                    else View.GONE

                when (destination.id) {
                    R.id.mainHomeFragment -> bnvMain.menu.findItem(R.id.item_home).isChecked = true
                    R.id.veganMapFragment -> bnvMain.menu.findItem(R.id.item_map).isChecked = true
                    R.id.mainTipsFragment -> bnvMain.menu.findItem(R.id.item_tips).isChecked = true
                    R.id.mainMypageFragment -> bnvMain.menu.findItem(R.id.item_profile).isChecked = true
                }
            }

            bnvMain.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> mainNavigationHandler.navigateToHome()
                    R.id.item_map -> mainNavigationHandler.navigateToMap()
                    R.id.item_tips -> mainNavigationHandler.navigateToTips()
                    R.id.item_profile -> mainNavigationHandler.navigateToMypage()
                }
                true
            }

        }
    }

    //BackStack
    private fun setupOnBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.dlDrawer.isDrawerOpen(GravityCompat.END)) {//Drawer open 여부 확인
                    binding.dlDrawer.closeDrawer(GravityCompat.END)
                } else if (navController.backQueue.size > 2) {
                    navController.popBackStack()
                } else {
                    isEnabled = false
                    finish()
                }
            }
        })
    }

    companion object{
        //FCM Intent 주입
        fun createIntent(
            context: Context,
            fcmData: FcmData
        ) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            this.putExtra("fcmData",fcmData)
        }

        //BottomNav Visibility 처리 - hashSet
        val bottomNavVisibilityMap = hashSetOf(
            R.id.mainHomeFragment,
            R.id.veganMapFragment,
            R.id.mainTipsFragment,
            R.id.mainMypageFragment,
            R.id.veganMapResultFragment
        )
    }
}