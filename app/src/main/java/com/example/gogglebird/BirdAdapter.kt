package com.example.gogglebird
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gogglebird.R

data class BirdAdapter (private val birdDataList: List<BirdDataItem>) :
RecyclerView.Adapter<BirdAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView){

        private val speciesCodeTextView: TextView = itemView.findViewById(R.id.speciesCodeTextView)
        private val commonNameTextView: TextView = itemView.findViewById(R.id.commonNameTextView)
        private val scientificNameTextView: TextView = itemView.findViewById(R.id.scientificNameTextView)
        private val predictionYearTextView: TextView = itemView.findViewById(R.id.predictionYearTextView)
        private val regionTypeTextView: TextView = itemView.findViewById(R.id.regionTypeTextView)
        private val regionCodeTextView: TextView = itemView.findViewById(R.id.regionCodeTextView)
        private val regionNameTextView: TextView = itemView.findViewById(R.id.regionNameTextView)
        private val seasonTextView: TextView = itemView.findViewById(R.id.seasonTextView)
        private val startDateTextView: TextView = itemView.findViewById(R.id.startDateTextView)
        private val endDateTextView: TextView = itemView.findViewById(R.id.endDateTextView)
        private val abundanceMeanTextView: TextView = itemView.findViewById(R.id.abundanceMeanTextView)
        private val totalPopPercentTextView: TextView = itemView.findViewById(R.id.totalPopPercentTextView)
        private val rangePercentOccTextView: TextView = itemView.findViewById(R.id.RangePercentOccTextView)
        private val rangeTotalPercentTextView: TextView = itemView.findViewById(R.id.RangeTotalPercentTextView)
        private val rangeDaysOccupationTextView: TextView = itemView.findViewById(R.id.RangeDaysOccTextView)

        fun bind(birdList: BirdDataItem) {
            speciesCodeTextView.text = "Species Code: ${birdList.species_code}"
            commonNameTextView.text = "Common Name:  ${birdList.common_name}"
            scientificNameTextView.text = "Scientific Name: ${birdList.scientific_name}"
            predictionYearTextView.text = "Prediction Year: ${birdList.prediction_year}"
            regionTypeTextView.text = "Region Type: ${birdList.region_type}"
            regionCodeTextView.text = "Region Code: ${birdList.region_code}"
            regionNameTextView.text = "Region Name: ${birdList.region_name}"
            seasonTextView.text = "Season: ${birdList.season}"
            startDateTextView.text = "Start Date: ${birdList.start_date}"
            endDateTextView.text = "End Date: ${birdList.end_date}"
            abundanceMeanTextView.text = "Abundance Mean: ${birdList.abundance_mean}"
            totalPopPercentTextView.text = "Total Pop Percent: ${birdList.total_pop_percent}"
            rangePercentOccTextView.text = "Range Percent Occupied: ${birdList.range_percent_occupied}"
            rangeTotalPercentTextView.text = "Range Total Percent: ${birdList.range_total_percent}"
            rangeDaysOccupationTextView.text = "Range Days Occupied: ${birdList.range_days_occupation}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bird_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return birdDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val birdList = birdDataList[position]
        holder.bind(birdList)
    }

}