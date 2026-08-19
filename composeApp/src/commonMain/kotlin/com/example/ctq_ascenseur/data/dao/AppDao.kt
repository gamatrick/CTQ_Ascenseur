package com.example.ctq_ascenseur.data.dao

import androidx.room.*
import com.example.ctq_ascenseur.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Elevator
    @Query("SELECT * FROM Elevator")
    fun getAllElevators(): Flow<List<Elevator>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElevator(elevator: Elevator)

    // Inspection
    @Query("SELECT * FROM Inspection ORDER BY date DESC")
    fun getAllInspections(): Flow<List<Inspection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: Inspection)

    @Query("SELECT * FROM Inspection WHERE id = :id")
    suspend fun getInspectionById(id: String): Inspection?

    // Dynamic Schema
    @Query("SELECT * FROM Section ORDER BY orderIndex ASC")
    fun getAllSections(): Flow<List<Section>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<Section>)

    @Query("SELECT * FROM ControlPointTemplate WHERE sectionId = :sectionId AND isEnabled = 1")
    fun getTemplatesForSection(sectionId: Int): Flow<List<ControlPointTemplate>>
    
    @Query("SELECT * FROM ControlPointTemplate WHERE isEnabled = 1")
    suspend fun getAllEnabledTemplates(): List<ControlPointTemplate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<ControlPointTemplate>)

    // Results
    @Query("SELECT * FROM InspectionResult WHERE inspectionId = :inspectionId")
    fun getResultsForInspection(inspectionId: String): Flow<List<InspectionResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: InspectionResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<InspectionResult>)
}
