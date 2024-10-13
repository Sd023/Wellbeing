package com.sdapps.wellbeing.mindfullness.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdapps.wellbeing.LoginActivity.Companion.CURRENT_DATE
import com.sdapps.wellbeing.R
import com.sdapps.wellbeing.wellness.WellnessActivity
import com.sdapps.wellbeing.databinding.ActivityMindfulnessBinding
import com.sdapps.wellbeing.mindfullness.data.MenuBO
import com.sdapps.wellbeing.mindfullness.manager.CustomView
import com.sdapps.wellbeing.wellness.WellnessActivity.Companion.activityTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.random.Random

class MindfulnessActivity : AppCompatActivity(), CustomView {

    private lateinit var binding: ActivityMindfulnessBinding
    private lateinit var adapter : MindfulAdapter

    private var isSyncDone: Boolean = false
    private var client: OkHttpClient = OkHttpClient()

    private var progressDialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMindfulnessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        val mainMenu = mutableListOf(
            MenuBO(BREATHE, 30,isTime = true,ContextCompat.getDrawable(this,R.drawable.icon1)?.toBitmap()),
            MenuBO(CHALLENGE,5, isTime = false, ContextCompat.getDrawable(this,R.drawable.task)?.toBitmap()),
            MenuBO(SYNC,0,false,ContextCompat.getDrawable(this,R.drawable.sync_icon)?.toBitmap())
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
        const val SYNC = "SYNC"
        const val ACTUAL = "actual"
        const val TARGET = "target"
    }

    private fun Drawable.toBitmap(): Bitmap{
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }

    override fun handleCardClick(moduleName: String, menuBO: MenuBO) {

        if(moduleName.equals(SYNC, ignoreCase = true)){
            if(isNetworkConnected()){
                prepareAndUpload()
            } else {
                Toast.makeText(this@MindfulnessActivity, "Connect to network", Toast.LENGTH_LONG).show()
            }

        } else {
            val intent = Intent(this@MindfulnessActivity,WellnessActivity::class.java).apply {
                putExtra(MODULE_CODE,moduleName)
                putExtra(MENUBO,menuBO)
            }
            startActivity(intent)
        }
    }

    private fun prepareAndUpload(){
        showLoading()
        val jsonArray = JSONArray()

//        id: { type: Number, required: true },
//        moduleCode: { type: String, required: true },
//        actual: { type: Number, required: true },
//        target: { type: Number, required: true },
//        date : {type: String, required: true}
        val sharedPreferences: SharedPreferences = getSharedPreferences(activityTracker, MODE_PRIVATE)
        val jsonObject1 = JSONObject().apply {
            put("id", Random.nextInt(0,9999))
            put(MODULE_CODE, BREATHE.replace("'", "\""))
            put(ACTUAL, sharedPreferences.getInt(BREATHE,0))
            put(TARGET, 20)
            put(CURRENT_DATE,sharedPreferences.getString(CURRENT_DATE,"")!!.replace("'", "\""))
        }

        val jsonObject2 = JSONObject().apply {
            put("id", Random.nextInt(0,9999))
            put(MODULE_CODE, CHALLENGE.replace("'", "\""))
            put(ACTUAL, sharedPreferences.getInt(CHALLENGE,0))
            put(TARGET, 50)
            put(CURRENT_DATE,sharedPreferences.getString(CURRENT_DATE,"")?.replace("'", "\""))
        }
        jsonArray.put(jsonObject1).put(jsonObject2)
        upload(jsonArray)
    }

    private fun upload(jsonArray: JSONArray){
        if (isNetworkConnected()){
            client = OkHttpClient()
            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = jsonArray.toString().toRequestBody(JSON)

            val request = Request.Builder()
                .url("http://10.0.2.2:3000/upload")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                       hideLoading()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                
            })

        }
    }

    private fun showLoading(){
        progressDialog!!.setTitle("Sync")
        progressDialog!!.setMessage("Uploading data to server")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }
    private fun hideLoading(){
        progressDialog!!.dismiss()
    }

    private fun isNetworkConnected(): Boolean{
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}