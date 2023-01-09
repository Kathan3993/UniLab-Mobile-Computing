package com.example.mobilecomputingproject.models

/**
 * Model for Laptop Information
 */
data class LaptopInformationItem(
    val laptop_id: String = "",
    val laptop_type: String = "",
    val ram: String = "",
    val storage: String = "",
    val lending_type: String = "",
    val is_booked: Boolean = false,
    val laptop_model: String = "",
    val library_id: String = "",
    val library: String = ""
)
