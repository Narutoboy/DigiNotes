package com.do_big.diginotes.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.do_big.diginotes.databinding.ActivitySplashScreenBinding
import com.do_big.diginotes.utils.PrefManager

class SplashScreen : AppCompatActivity() {

    companion object {
        const val SPLASH_DELAY = 2500L // Use Long for milliseconds
        const val WELCOME_DELAY = 1000L
    }

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefManager = PrefManager(this)
        val isFirstTimeLaunch = prefManager.isFirstTimeLaunch

        val delay = if (isFirstTimeLaunch) WELCOME_DELAY else SPLASH_DELAY
        val targetActivity =
            if (isFirstTimeLaunch) WelcomeActivity::class.java else HomeActivity::class.java

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, targetActivity)
            startActivity(intent)
            finish()
        }, delay)
    }
}