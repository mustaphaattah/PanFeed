package com.mtah.panfeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.lang.Exception
import java.lang.Thread.sleep

class SplashScreen : AppCompatActivity() {

    var TAG = "SplashScreen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Thread( Runnable {
            try {
                sleep(1500)
                val homeIntent = Intent(this, MainActivity::class.java)
                startActivity(homeIntent)
                finish()
            } catch (e: Exception){
                Log.e(TAG, e.localizedMessage)
                e.printStackTrace()
            }
        }).start()
    }
}
