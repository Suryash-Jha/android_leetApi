package com.example.leetstalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import android.view.inputmethod.InputMethodManager
import android.content.Context



class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn= findViewById<Button>(R.id.submitButton)
        btn.setOnClickListener {
            val focusedView = currentFocus

            // Create an InputMethodManager instance
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // Hide the keyboard
            inputMethodManager.hideSoftInputFromWindow(focusedView?.windowToken, 0)

            val leet_id= findViewById<EditText>(R.id.git_id).text
            val easyText= findViewById<TextView>(R.id.easyCount)
            val medText= findViewById<TextView>(R.id.medCount)
            val hardText= findViewById<TextView>(R.id.hardCount)
            val statusView= findViewById<TextView>(R.id.statusView)
            val ratingText= findViewById<TextView>(R.id.ratingCount)


            val gson= GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create()


            val okHttpClient= OkHttpClient()
            val request= Request.Builder()
                .url("https://taquamediumorchidvoxels.suryashjha.repl.co/leetApi/${leet_id}")
                .build()
            GlobalScope.launch {
                val response= withContext(Dispatchers.IO){okHttpClient.newCall(request).execute().body?.string()}
                val data= gson.fromJson<LeetData>(response, LeetData::class.java)
                var easy = 0
                var medium = 0
                var hard = 0
                var rating = 0

                if (data.status == 200) {
                    easy = data.easy!!
                    medium = data.medium!!
                    hard = data.hard!!
                    rating= data.rating!!

                }


//https://suryash.pythonanywhere.com/leetApi/<str:id>

                Log.i("networking", "${response}")
                runOnUiThread {
                    if(data.status== 200){
                        statusView.text="Details Found!"
                        statusView.setBackgroundColor(android.graphics.Color.parseColor("#00FF00"))

                    }
                    else{
                        statusView.text="User Doesn't Exist!"
                        statusView.setBackgroundColor(android.graphics.Color.parseColor("#FF0000"))
                    }
                    easyText.text = easy.toString()
                    medText.text = medium.toString()
                    hardText.text = hard.toString()
                    ratingText.text= rating.toString()

                }
            }
        }

        }
    }
