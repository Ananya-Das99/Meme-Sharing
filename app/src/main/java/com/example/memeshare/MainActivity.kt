package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject
import java.lang.ref.ReferenceQueue as ReferenceQueue1

class MainActivity : AppCompatActivity() {
    var currentImageUrl:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

     fun loadMeme(){
         val progressBar = findViewById<ProgressBar>(R.id.progressBar)
         progressBar.visibility = View.VISIBLE
         //val textView = findViewById<TextView>(R.id.text)
         // ...

         // Instantiate the RequestQueue.
         val queue = Volley.newRequestQueue(this)
         val url = "https://meme-api.herokuapp.com/gimme"

         // Request a string response from the provided URL.
         val jsonObjectRequest= JsonObjectRequest(Request.Method.GET ,url,null ,
             { response->
                 currentImageUrl =  response.getString("url")
                 val memeImageView = findViewById<ImageView>(R.id.memeImageView)
                 Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean
                     ): Boolean {
                         progressBar.visibility = View.GONE
                         return true
                     }

                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {
                         progressBar.visibility=View.GONE
                         return false
                     }
                 }).into(memeImageView)
                 // Display the first 500 characters of the response string.
                 //textView.text = "Response is: ${response.substring(0, 500)}"
             },
             {
                 Toast.makeText(this,"Something went Wrong",Toast.LENGTH_LONG).show()
             })


         // Add the request to the RequestQueue.
         queue.add(jsonObjectRequest)

     }
    fun shareMeme(view: View) {
       val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
       intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}