package com.ponomarev.ruler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import com.ponomarev.ruler.custom_views.Ruler.Position.LEFT
import com.ponomarev.ruler.custom_views.Ruler.Position.RIGHT
import com.ponomarev.ruler.data.DataRepository
import com.ponomarev.ruler.data.DataRepository.darkTheme
import com.ponomarev.ruler.databinding.ActivityRulerBinding


class RulerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRulerBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.darker_gray)
        window.navigationBarColor = ContextCompat.getColor(
            this,
            android.R.color.darker_gray
        )
        setDefaultNightMode(if (darkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        with(binding) {
            menuAnchor.setOnClickListener { popupMenu(menuAnchor) }
            rulerLeft.side = LEFT
            rulerRight.side = RIGHT
            calibrationView.measureCallback = { height ->
                rulerLeft.calibrated10cmHeight = height
                rulerRight.calibrated10cmHeight = height
                val param = DataRepository.calParameter
                rulerLeft.calParameter = param
                rulerRight.calParameter = param
                slider.value = param
            }
            slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
                rulerLeft.calParameter = value
                rulerRight.calParameter = value
                DataRepository.calParameter = value
            })
            finishCalibration.setOnClickListener {
                slider.isVisible = false
                finishCalibration.isVisible = false
                instruction.isVisible = false

            }
        }
    }


    private fun popupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
        popupMenu.setForceShowIcon(true)
        if (darkTheme) popupMenu.menu.findItem(R.id.theme)?.apply {
            this.setTitle(getString(R.string.light_theme))
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.calibrate_menu -> calibrate()
                R.id.theme -> changeTheme()
                R.id.info_menu -> startActivity(Intent(this, InfoActivity::class.java))
            }
            true
        }
        popupMenu.show()
    }

    private fun changeTheme() {
        darkTheme = !darkTheme
        setDefaultNightMode(if (darkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }

    private fun calibrate() {
        with(binding) {
            slider.isVisible = true
            finishCalibration.isVisible = true
            instruction.isVisible = true
        }
    }

    private fun shareApp() {
        val shareIntent = Intent()
        val url = "https://play.google.com/store/apps/details?id=com.ponomarev.ruler"
        shareIntent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/html"
        }
        startActivity(Intent.createChooser(shareIntent, getText(R.string.sendTo)))
    }

}