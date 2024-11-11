package com.sdapps.wellbeing.core

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.sdapps.wellbeing.R


class CustomProgressDialog(private val appContext: Context) {

    private var progressDialog: AlertDialog? = null

    fun showDialog(){
        progressDialog = AlertDialog.Builder(appContext)
            .setView(R.layout.custom_progress_layout)
            .setCancelable(false)
            .show()
    }

    fun dismissDialog() {
        try {
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }
}