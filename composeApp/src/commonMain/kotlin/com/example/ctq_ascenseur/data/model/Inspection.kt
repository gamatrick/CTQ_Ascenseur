package com.example.ctq_ascenseur.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class InspectionStatus {
    DRAFT, COMPLETED, ARCHIVED
}

@Entity
@Serializable
data class Inspection(
    @PrimaryKey val id: String,
    val elevatorId: String,
    val date: Long,
    val technicianName: String,
    val status: InspectionStatus
)
