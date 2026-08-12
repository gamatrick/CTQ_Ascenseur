package com.example.ctq_ascenseur.data.model

import androidx.room.Entity

@Entity(tableName = "inspection_results", primaryKeys = ["inspectionId", "controlPointId"])
data class InspectionResult(
    val inspectionId: String,
    val controlPointId: Int,
    val status: ControlResultStatus,
    val comment: String? = null,
    val photoPath: String? = null
)

enum class ControlResultStatus {
    CONFORME, NON_CONFORME, SANS_OBJET, NON_VERIFIABLE
}
