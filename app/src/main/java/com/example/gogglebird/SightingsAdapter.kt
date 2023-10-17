package com.example.gogglebird

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SightingsAdapter(
    private var sightingsList: List<Sightings>,
    private val itemClickListener: SavedSightingsPage
) : RecyclerView.Adapter<SightingsAdapter.SightingsViewHolder>() {

    fun setData(newSightingsList: List<Sightings>) {
        sightingsList = newSightingsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightingsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sightings_item, parent, false)
        return SightingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SightingsViewHolder, position: Int) {
        val sighting = sightingsList[position]
        holder.bind(sighting)

        val imageViewItem = holder.itemView.findViewById<ImageView>(R.id.imageViewItem)

        // Load the image using Picasso (you may need to adjust this based on your image path field)
        Picasso.get()
            .load(sighting.imagePath)
            .into(imageViewItem)
    }

    override fun getItemCount(): Int {
        return sightingsList.size
    }

    inner class SightingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvEntryName: TextView = itemView.findViewById(R.id.tvEntryName)
        private val tvSpeciesName: TextView = itemView.findViewById(R.id.tvSpecies)
        private val tvLat: TextView = itemView.findViewById(R.id.tvCurrentLatitude)
        private val tvLong: TextView = itemView.findViewById(R.id.tvCurrentLongitude)
        private val tvNumBirds: TextView = itemView.findViewById(R.id.tvNumBirds)
        private val tvExtraInfo: TextView = itemView.findViewById(R.id.tvExtraInfo)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val sighting = sightingsList[position]
                    itemClickListener.onItemClick(sighting)
                }
            }
        }

        fun bind(sighting: Sightings) {

            tvEntryName.text = "${sighting.entryName}"
            tvSpeciesName.text = "Species: ${sighting.speciesName}"
            tvLat.text = "Location: ${sighting.currentLatitude}"
            tvLong.text = "Location: ${sighting.currentLongitude}"
            tvNumBirds.text = "Number of Birds: ${sighting.NumBirds}"
            tvExtraInfo.text = "Extra Info: ${sighting.extraInfo}"
        }
    }
}
