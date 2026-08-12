package com.example.ctq_ascenseur.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "elevators")
@Serializable
data class Elevator(
    @PrimaryKey val id: String,
    val address: String,
    val brand: String,
    val type: String,
    val serialNumber: String,
    val manufactureYear: Int,
    val loadCapacity: Int, // kg
    val speed: Double, // m/s
    val isCeMarked: Boolean
)
