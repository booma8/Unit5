package com.example.pet

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.uuid.Uuid.Companion.random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val stupidAssButton = findViewById<Button>(R.id.petButton)
        setupButton(stupidAssButton)
        fetchImage()
    }

    private fun fetchImage() {
        val client = AsyncHttpClient()
        val randomNum = (11..100).random()
        val imageURL =  "https://picsum.photos/id/${randomNum}/info"
        client[imageURL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val imageURL = json.jsonObject.getString("download_url")
                val imageView = findViewById<ImageView>(R.id.imagePet)
                Log.d("Dog", "response successful: $json")
                Log.d("imageURL", "pet image URL set: $imageURL")
                Glide.with(this@MainActivity)
                    .load(imageURL)
                    .fitCenter()
                    .into(imageView)

                val textView1 = findViewById<TextView>(R.id.urlView)
                imageURL.also { textView1.text = listOf("URL for this image is:\n", it).joinToString() }

                val textView2 = findViewById<TextView>(R.id.authorView)
                val imageAuthor = json.jsonObject.getString("author")
                imageAuthor.also { textView2.text = listOf("Author for this image is: ", it).joinToString() }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }

    private fun setupButton(button: Button) {
        button.setOnClickListener {
            fetchImage()
        }
    }
}