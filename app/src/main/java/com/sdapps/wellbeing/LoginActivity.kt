package com.sdapps.wellbeing


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdapps.wellbeing.databinding.ActivityMainBinding
import com.sdapps.wellbeing.mindfullness.ui.MindfulnessActivity


class LoginActivity : AppCompatActivity() {


    companion object {
        const val LOGIN_ID_LABEL = "LOGIN"
        const val LOGIN_PD_LABEL = "PASSWORD"


        const val LOGIN_ID = "test@gmail.com"
        const val LOGIN_PASSWORD = "test"
        const val CURRENT_DATE = "date"


        const val loginSharePref = "login_id"
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var loginSharedPreferences: SharedPreferences
    private lateinit var spEditor : SharedPreferences.Editor
    private var edtEmailText: String = ""
    private var edtPasswordText : String= ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginSharedPreferences = applicationContext.getSharedPreferences(loginSharePref, Context.MODE_PRIVATE)
        spEditor = loginSharedPreferences.edit()


        spEditor.putString(LOGIN_ID_LABEL,LOGIN_ID)
        spEditor.putString(LOGIN_PD_LABEL, LOGIN_PASSWORD)
        spEditor.apply()


        binding.login.setOnClickListener { handleLogin() }
    }


    private fun handleLogin(){
        if(!binding.email.text.isNullOrEmpty() || !binding.password.text.isNullOrEmpty()){
            edtEmailText = binding.email.text.trim().toString()
            edtPasswordText = binding.password.text.trim().toString()
            prepareLoginCredentials(edtEmailText,edtPasswordText)
        } else{
            Toast.makeText(this@LoginActivity,"Enter Credentials", Toast.LENGTH_LONG).show()
        }
    }


    private fun prepareLoginCredentials(email: String, password: String){
        val sharedPreferences = getSharedPreferences(loginSharePref, Context.MODE_PRIVATE)
        val spEmail = sharedPreferences.getString(LOGIN_ID_LABEL, "")
        val spPassword = sharedPreferences.getString(LOGIN_PD_LABEL, "")


        if(spEmail.equals(email, ignoreCase = true)
            && spPassword.equals(password, ignoreCase = true)){


            val intent = Intent(this@LoginActivity, MindfulnessActivity::class.java)
            startActivity(intent)


        } else {
            Toast.makeText(this,"Invalid Credentials", Toast.LENGTH_LONG).show()
        }
    }
}
