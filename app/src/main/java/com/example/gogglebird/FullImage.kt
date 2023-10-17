package com.example.gogglebird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class FullImage : AppCompatActivity() {
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)

        imageView = findViewById(R.id.imageViewFull)

        val imagePath = intent.getStringExtra("imagePath")

        // Load the image using Picasso
        Picasso.get()
            .load(imagePath)
            .into(imageView)
    }
}
