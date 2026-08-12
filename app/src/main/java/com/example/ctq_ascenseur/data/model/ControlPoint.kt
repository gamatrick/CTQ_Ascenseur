package com.example.ctq_ascenseur.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "control_points")
data class ControlPoint(
    @PrimaryKey val id: Int,
    val section: String,
    val name: String,
    val description: String,
    val isMandatory: Boolean = true
)
