package com.example.gogglebird
import java.util.Date

data class Sightings(
    val entryId: String? = null,
    val entryName: String? = null,
    val currentLatitude: String? = null,
    val currentLongitude: String? = null,
    val speciesName: String? = null,
    val Date: Date? = null,
    val Time: Date? = null,
    val NumBirds: String? = null,
    val extraInfo: String? = null,
    var imagePath: String? = null,
    val userId: String? = null
)
