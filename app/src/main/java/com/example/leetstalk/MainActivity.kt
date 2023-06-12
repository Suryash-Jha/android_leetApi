package com.example.leetstalk

import androidx.appcompat.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod;
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

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn= findViewById<Button>(R.id.submitButton)
        btn.setOnClickListener {
            val git_id= findViewById<EditText>(R.id.git_id).text
            val textView= findViewById<TextView>(R.id.apiResp)
            val okHttpClient= OkHttpClient()
            val request= Request.Builder()
                .url("https://api.github.com/users/${git_id}")
                .build()
            GlobalScope.launch {
                val response= withContext(Dispatchers.IO){okHttpClient.newCall(request).execute()}
                Log.i("networking", "${response.body}")
                textView.text= response.body?.string()
            }
            textView.movementMethod = ScrollingMovementMethod()
        }

        }
    }
