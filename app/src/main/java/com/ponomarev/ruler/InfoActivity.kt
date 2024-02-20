package com.ponomarev.ruler

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ponomarev.ruler.databinding.ActivityInfoBinding
import com.ponomarev.ruler.databinding.ActivityRulerBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.email.setOnClickListener { sendMail() }
        binding.email.setEndIconOnClickListener { sendMail() }
        binding.input.setOnClickListener { sendMail() }
    }

    private fun sendMail() {
        val emailIntent = Intent()
        val topic = getString(R.string.about_the_ruler_android_application)
        emailIntent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, topic)
            data = Uri.parse("mailto:461300@mail.ru")
        }
        startActivity(Intent.createChooser(emailIntent, getText(R.string.sendTo)))
    }
}