package com.example.ctq_ascenseur.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Section(
    @PrimaryKey val id: Int,
    val title: String,
    val orderIndex: Int
)

@Entity
@Serializable
data class ControlPointTemplate(
    @PrimaryKey val id: Int,
    val sectionId: Int,
    val title: String,
    val description: String,
    val isEnabled: Boolean = true
)

enum class ControlResultStatus {
    CONFORME, NON_CONFORME, SANS_OBJET, NON_VERIFIABLE
}

enum class SeverityLevel {
    NONE, OBSERVATION, PRESCRIPTION, DGI
}

@Entity(primaryKeys = ["inspectionId", "templateId"])
@Serializable
data class InspectionResult(
    val inspectionId: String,
    val templateId: Int,
    val status: ControlResultStatus,
    val severity: SeverityLevel = SeverityLevel.NONE,
    val comment: String = ""
)
