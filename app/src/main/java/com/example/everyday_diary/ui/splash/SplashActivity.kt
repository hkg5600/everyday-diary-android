package com.example.everyday_diary.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.everyday_diary.ui.main.MainActivity
import com.example.everyday_diary.utils.FileManager
import com.example.everyday_diary.network.response.TokenResponse
import com.example.everyday_diary.utils.TokenObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileManager.deleteCache(applicationContext)
        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "tokenData" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        viewModel.message.observe(this, Observer {
            when (it) {
                "토큰 사용 가능" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        viewModel.error.observe(this, Observer {
            when (it) {
                "조작된 토큰입니다" -> {
                    //startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                "만료된 토큰입니다" -> viewModel.refreshToken(TokenObject.refreshToken!!)
                //"만료된 리프레쉬 토큰입니다" -> startActivity(Intent(this, LoginActivity::class.java))
            }
        })

        viewModel.data.observe(this, Observer {
            when (it) {
                is TokenResponse -> {
                    if (it.token != null) {
                        TokenObject.token = it.token
                        viewModel.insertToken(TokenObject.token!!, TokenObject.refreshToken!!)
                    } else {
                        Toast.makeText(this, "세션이 만료되었습니다", Toast.LENGTH_SHORT).show()
                        //startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        })

        viewModel.tokenSuccess.observe(this, Observer {
            TokenObject.token?.let {
                viewModel.verifyToken(it)
            }
        })

        viewModel.tokenError.observe(this, Observer {
            Toast.makeText(this, "로그인 해주세요", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        viewModel.networkError.observe(this, Observer {
            Toast.makeText(this, "네트워크 연결 후 시도해주세요", Toast.LENGTH_SHORT).show()
            finish()
        })

        viewModel.getToken()
    }
}
