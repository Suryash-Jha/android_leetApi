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
import android.content.Intent
import android.net.Uri
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.submitButton)
        val gfgbtn = findViewById<Button>(R.id.gfgButton)
        btn.setOnClickListener {
            showResults()
        }
        gfgbtn.setOnClickListener {
            val i= Intent(this, MainActivity2::class.java)
            startActivity(i)
        }
    }
    fun showResults() {
        val focusedView = currentFocus
        // Create an InputMethodManager instance
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Hide the keyboard
        inputMethodManager.hideSoftInputFromWindow(focusedView?.windowToken, 0)

        val leet_id = findViewById<EditText>(R.id.git_id).text
        val easyText = findViewById<TextView>(R.id.easyCount)
        val medText = findViewById<TextView>(R.id.medCount)
        val hardText = findViewById<TextView>(R.id.hardCount)
        val statusView = findViewById<TextView>(R.id.statusView)
        val ratingText = findViewById<TextView>(R.id.ratingCount)
        val urlText = findViewById<TextView>(R.id.urlText)
        val totalText = findViewById<TextView>(R.id.totalText)


        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create()


        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://taquamediumorchidvoxels.suryashjha.repl.co/leetApi/${leet_id}")
            .build()
        GlobalScope.launch {
            val response: String? = try {
                withContext(Dispatchers.IO) {
                    okHttpClient.newCall(request).execute().body?.string()
                }
            } catch (e: Exception) {
                "{\"status\": 502 }"

            }
            val data = gson.fromJson<LeetData>(response, LeetData::class.java)
            var easy = 0
            var medium = 0
            var hard = 0
            var rating = 0
            var url = ""
            var total = 0

            if (data.status == 200) {
                easy = data.easy!!
                medium = data.medium!!
                hard = data.hard!!
                rating = data.rating!!
                url = data.userProfileLink!!
                total = data.total!!

            }


//https://suryash.pythonanywhere.com/leetApi/<str:id>

            Log.i("networking", "${response}")
            runOnUiThread {
                if (data.status == 200) {
                    statusView.text = "Details Found!"
                    statusView.setBackgroundColor(android.graphics.Color.parseColor("#00FF00"))

                } else if (data.status == 404) {
                    statusView.text = "User Doesn't Exist!"
                    statusView.setBackgroundColor(android.graphics.Color.parseColor("#FF0000"))
                } else {
                    statusView.text = "Data Fetching Failed!"
                    statusView.setBackgroundColor(android.graphics.Color.parseColor("#FFA500"))
                }
                easyText.text = easy.toString()
                medText.text = medium.toString()
                hardText.text = hard.toString()
                ratingText.text = rating.toString()
                urlText.text = url
                totalText.text = total.toString()
                urlText.setOnClickListener {
                    redirectToBrowser(url)
                }

            }
        }
    }
    fun redirectToBrowser(t: String) {
        if (t.length > 0) {
            val url = "https://${t}"
            Toast.makeText(this, "Redirecting to Profile Page", Toast.LENGTH_SHORT).show()
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
    }
}
