package com.beginvegan.presentation.view.login.view

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.beginvegan.presentation.base.BaseActivity
import com.beginvegan.presentation.databinding.ActivityLoginBinding
import com.beginvegan.presentation.view.login.viewModel.LoginViewModel
import com.beginvegan.presentation.view.main.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    override fun inflateBinding(layoutInflater: LayoutInflater) = ActivityLoginBinding.inflate(layoutInflater)

    override fun initViewModel() {
        viewModel.loginState.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                if (viewModel.additionalInfoProvided) {
                    navigateToMainActivity()
                } else {
                    navigateToOnboardingActivity()
                }
            }
        }
        viewModel.checkFirstRun()
        viewModel.isFirstRun.observe(this) {
            if (it) {
                // 첫 실행 o
                viewModel.fetchFirstRunRecord()
                NoticePermissionDialog().show(supportFragmentManager, "PermissionDialog")
                logMessage("initViewModel: first run check ${viewModel.isFirstRun.value}")
            }else{
                // 첫 실행 x
                logMessage("initViewModel: first run check ${viewModel.isFirstRun.value}")
            }
        }
    }

    override fun init() {
        setOnClickLogin()
    }

    private fun setOnClickLogin() {
        binding.btnLoginKakao.setOnClickListener {
            viewModel.kakaoLogin(this)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
    }
}