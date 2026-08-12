package com.example.ctq_ascenseur.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspections")
data class Inspection(
    @PrimaryKey val id: String,
    val elevatorId: String,
    val date: Long,
    val technicianName: String,
    val status: InspectionStatus,
    val signaturePath: String? = null
)

enum class InspectionStatus {
    DRAFT, COMPLETED, SYNCED
}
