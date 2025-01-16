package com.twomuchcar.usedcar.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.twomuchcar.usedcar.R
import com.twomuchcar.usedcar.firebase.ApiClient
import com.twomuchcar.usedcar.firebase.TokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 알림 허용 수락
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // FCM 토큰 발급 및 서버로 전송
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token: ${task.result}")
                Log.d("FCM", "Token: $token") // Logcat에서 확인 가능
                sendTokenToServer(token)
            } else {
                Log.e("FCM", "Token error: ${task.exception}")
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

    }

    // 서버로 FCM 토큰 전송
    private fun sendTokenToServer(token: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.api.updateFcmToken(TokenRequest(token))
                if (!response.isSuccessful) {
                    Log.e("FCM", "Failed to send token: ${response.message()}")
                } else {
                    Log.d("FCM", "Token sent successfully")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error sending token", e)
            }
        }
    }
}