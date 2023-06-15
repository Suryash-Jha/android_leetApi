package com.example.leetstalk

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val btn = findViewById<Button>(R.id.submitButton)
        val gfgbtn = findViewById<Button>(R.id.leetButton)
        btn.setOnClickListener {
            showResults()
        }
        gfgbtn.setOnClickListener {
            val i= Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
    fun showResults() {
        val focusedView = currentFocus
        // Create an InputMethodManager instance
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // Hide the keyboard
        inputMethodManager.hideSoftInputFromWindow(focusedView?.windowToken, 0)

        val gfg_id = findViewById<EditText>(R.id.git_id).text
        val schoolText = findViewById<TextView>(R.id.schoolCount)
        val basicText = findViewById<TextView>(R.id.basicCount)
        val easyText = findViewById<TextView>(R.id.easyCount)
        val medText = findViewById<TextView>(R.id.medCount)
        val hardText = findViewById<TextView>(R.id.hardCount)
        val statusView = findViewById<TextView>(R.id.statusView)
        val totalScoreText = findViewById<TextView>(R.id.totalScoreText)
        val totalProblemText = findViewById<TextView>(R.id.totalProblemText)
        val urlText = findViewById<TextView>(R.id.urlText)


        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create()


        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://taquamediumorchidvoxels.suryashjha.repl.co/gfgApi/${gfg_id}")
            .build()
        GlobalScope.launch {
            val response: String? = try {
                withContext(Dispatchers.IO) {
                    okHttpClient.newCall(request).execute().body?.string()
                }
            } catch (e: Exception) {
                "{\"status\": 502 }"

            }
            val data = gson.fromJson<GfgData>(response, GfgData::class.java)
            var school= "0"
            var basic="0"
            var easy = "0"
            var medium = "0"
            var hard = "0"
            var url = ""
            var totalQues = "0"
            var totalScore= "0"
            Log.i("status", "${data.Status}")

            if (data.Status == 200) {
                school= data.SCHOOL!!
                basic= data.BASIC!!
                easy = data.EASY!!
                medium = data.MEDIUM!!
                hard = data.HARD!!
                totalQues = data.TotalProblemSolved!!
                totalScore= data.TotalScore!!

            }


//https://suryash.pythonanywhere.com/leetApi/<str:id>

            Log.i("networking", "${response}")
            runOnUiThread {
                if (data.Status == 200) {
                    statusView.text = "Details Found!"
                    statusView.setBackgroundColor(Color.parseColor("#00FF00"))

                } else if (data.Status == 404) {
                    statusView.text = "User Doesn't Exist!"
                    statusView.setBackgroundColor(Color.parseColor("#FF0000"))
                } else {
                    statusView.text = "Data Fetching Failed!"
                    statusView.setBackgroundColor(Color.parseColor("#FFA500"))
                }
                schoolText.text = school
                basicText.text = basic
                easyText.text = easy
                medText.text = medium
                hardText.text = hard
                urlText.text = url
                totalScoreText.text = totalScore
                totalProblemText.text = totalQues

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