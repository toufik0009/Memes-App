package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity() : AppCompatActivity() {
    var currentImageUrl:Any?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()


    }

    private fun loadMeme(){
        //Progress bar show
        val progress=findViewById<LottieAnimationView>(R.id.progress)
        Handler(Looper.getMainLooper()).postDelayed({
            progress.visibility= View.VISIBLE
            progress.playAnimation()
        },0)


        //create queue
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"
        //Api call process
        val jsonObjectRequest = JsonObjectRequest (
            Request.Method.GET, url, null,
            Response.Listener{ response ->
              currentImageUrl = response.getString("url")
                var memeImageView:ImageView= findViewById(R.id.memeImageView)
                Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>  {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       progress.visibility= View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility= View.GONE
                        return false
                    }
                }).into(memeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went Wrong",Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)
    }
     fun shareMeme(view: View) {
       val intent = Intent(Intent.ACTION_SEND)
         intent.type="text/plain"
         intent.putExtra(Intent.EXTRA_TEXT,"Hey Check this Cool Memes:- $currentImageUrl")

        val chooser= Intent.createChooser(intent,"Share this meme Using...")

         startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}