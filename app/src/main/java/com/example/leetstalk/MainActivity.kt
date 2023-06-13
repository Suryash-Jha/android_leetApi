package com.example.leetstalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn= findViewById<Button>(R.id.submitButton)
        btn.setOnClickListener {
            val leet_id= findViewById<EditText>(R.id.git_id).text
            val easyText= findViewById<TextView>(R.id.easyCount)
            val medText= findViewById<TextView>(R.id.medCount)
            val hardText= findViewById<TextView>(R.id.hardCount)

            val okHttpClient= OkHttpClient()
            val request= Request.Builder()
                .url("http://192.168.1.4:8000/${leet_id}")
                .build()
            GlobalScope.launch {
                val response= withContext(Dispatchers.IO){okHttpClient.newCall(request).execute().body}
                val JsonData= JSONObject(response?.string())
                val easy= JsonData.getString("Easy")
                val medium= JsonData.getString("Medium")
                val hard= JsonData.getString("Hard")

                Log.i("networking", "${response}")
                runOnUiThread {
                    easyText.text = easy
                    medText.text = medium
                    hardText.text = hard

                }
            }
        }

        }
    }
