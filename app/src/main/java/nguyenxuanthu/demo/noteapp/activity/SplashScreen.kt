package nguyenxuanthu.demo.noteapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import nguyenxuanthu.demo.noteapp.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        supportActionBar?.title = "Notes App"

        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
            finish()
        },3000)
    }
}