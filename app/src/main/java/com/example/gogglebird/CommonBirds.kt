package com.example.gogglebird

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.IOException

class CommonBirds : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_birds)
        val jsonFileName = "data.json"
        val json: String? = readJsonFileFromAssets(jsonFileName)

        if (json != null) {
            val birdDataList = Gson().fromJson(json, Array<BirdDataItem>::class.java)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BirdAdapter(birdDataList.toList())
        }
    }

    private fun readJsonFileFromAssets(fileName: String): String? {
        val assetManager: AssetManager = assets
        val json: String

        try{
            val inputStream = assetManager.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch(e: IOException) {
            e.printStackTrace()
            return null
        }

        return json

    }
}