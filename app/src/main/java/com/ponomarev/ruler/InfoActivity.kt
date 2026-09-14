package com.ponomarev.ruler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ponomarev.ruler.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            email.setEndIconOnClickListener { sendMail() }
            input.setOnClickListener { sendMail() }
        }
    }

    private fun sendMail() {
        val emailAddress = getString(R.string.at_461300_mail_ru)
        val subject = getString(R.string.about_the_ruler_android_application)
        
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$emailAddress".toUri()
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        try {
            val chooserTitle = getString(R.string.sendTo)
            startActivity(Intent.createChooser(emailIntent, chooserTitle))
        } catch (_: Exception) {
            Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show()
        }
    }
}
