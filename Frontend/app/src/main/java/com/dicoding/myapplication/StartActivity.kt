package com.dicoding.myapplication


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Use Handler to delay for 3 seconds (3000 milliseconds)
        Handler().postDelayed({
            // Start LoginActivity after 3 seconds
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finish StartActivity so it doesn't appear in the back stack
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}
