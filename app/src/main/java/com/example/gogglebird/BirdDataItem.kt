package com.example.gogglebird

data class BirdDataItem(
    val abundance_mean: Double,
    val common_name: String,
    val end_date: Double,
    val prediction_year: Int,
    val range_days_occupation: String,
    val range_percent_occupied: Double,
    val range_total_percent: String,
    val region_code: String,
    val region_name: String,
    val region_type: String,
    val scientific_name: String,
    val season: String,
    val species_code: String,
    val start_date: Double,
    val total_pop_percent: String
)