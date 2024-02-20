package com.ponomarev.ruler

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.slider.Slider
import com.ponomarev.ruler.cusom_views.CalibrationView
import com.ponomarev.ruler.cusom_views.Ruler
import com.ponomarev.ruler.cusom_views.Ruler.Position.LEFT
import com.ponomarev.ruler.cusom_views.Ruler.Position.RIGHT
import com.ponomarev.ruler.data.DataRepository

class RulerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(
            this,
            android.R.color.transparent
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        val rulerLeft = findViewById<Ruler>(R.id.rulerLeft)
        val rulerRight = findViewById<Ruler>(R.id.rulerRight)
        val calibrationView = findViewById<CalibrationView>(R.id.calibrationSquare)
        val slider = findViewById<Slider>(R.id.slider)
        val hideButton = findViewById<Button>(R.id.finishCalibration)
        val menu = findViewById<ImageButton>(R.id.menuAnchor)
        val instruction = findViewById<TextView>(R.id.instruction)
        menu.setOnClickListener { popupMenu(menu) }
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
        rulerLeft.calParameter = 1f
        slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            rulerLeft.calParameter = value
            rulerRight.calParameter = value
            DataRepository.calParameter = value
        })
        hideButton.setOnClickListener {
            slider.isVisible = false
            hideButton.isVisible = false
            instruction.isVisible =false

        }
    }


    private fun popupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.calibrate_menu -> calibrate()
                R.id.share_menu -> shareApp()
                R.id.info_menu -> showInfo()
            }
            true
        }
        popupMenu.show()
    }

    private fun calibrate() {
        val instruction = findViewById<TextView>(R.id.instruction)
        val slider = findViewById<Slider>(R.id.slider)
        val hideButton = findViewById<Button>(R.id.finishCalibration)
        slider.isVisible = true
        hideButton.isVisible = true
        instruction.isVisible = true

    }

    private fun shareApp() {
        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)
        val url = "https://play.google.com/store/apps/details?id=com.ponomarev.ruler"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.setType("text/html")
        startActivity(
            Intent.createChooser(
                shareIntent,
                getText(R.string.sendTo)
            )
        )
    }

    private fun showInfo() {
        startActivity(Intent(this, InfoActivity::class.java))
    }


}