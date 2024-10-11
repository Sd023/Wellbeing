package com.sdapps.wellbeing.mindfullness.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.wellbeing.R
import com.sdapps.wellbeing.wellness.WellnessActivity
import com.sdapps.wellbeing.databinding.ActivityMindfulnessBinding
import com.sdapps.wellbeing.mindfullness.data.MenuBO
import com.sdapps.wellbeing.mindfullness.manager.CustomView

class MindfulnessActivity : AppCompatActivity(), CustomView {

    private lateinit var binding: ActivityMindfulnessBinding
    private lateinit var adapter : MindfulAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMindfulnessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainMenu = arrayListOf(
            MenuBO(BREATHE, 10,isTime = true,ContextCompat.getDrawable(this,R.drawable.icon1)?.toBitmap()),
            MenuBO(CHALLENGE,5, isTime = false, ContextCompat.getDrawable(this,R.drawable.task)?.toBitmap())
        )
        adapter = MindfulAdapter(mainMenu,this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter


    }

    companion object {
        const val MODULE_CODE = "moduleCode"
        const val MENUBO = "key"

        const val BREATHE = "Breathe"
        const val CHALLENGE = "Challenges"
    }

    private fun Drawable.toBitmap(): Bitmap{
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }

    override fun handleCardClick(moduleName: String, menuBO: MenuBO) {
        val intent = Intent(this@MindfulnessActivity,WellnessActivity::class.java).apply {
            putExtra(MODULE_CODE,moduleName)
            putExtra(MENUBO,menuBO)
        }
        startActivity(intent)
    }
}