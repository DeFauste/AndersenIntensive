package com.example.drawimageone

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.drawimageone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadImage.setOnClickListener {
            val url: String = binding.editUrl.text.toString()
            if (url.isNotEmpty()) {
                loadImage(url)
            } else {
                Toast.makeText(this, getString(R.string.EnterUrl), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("CheckResult")
    fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .error(R.drawable.ic_failed_load_foreground)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    p0: GlideException?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: Boolean
                ): Boolean {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.FailedLoad),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    Log.d("LOAD", "OnResourceReady")
                    // do something when picture already loaded
                    return false
                }
            }).into(binding.imageView)
    }
}
