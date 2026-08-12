package com.example.ctq_ascenseur.data.dao

import androidx.room.*
import com.example.ctq_ascenseur.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Elevators
    @Query("SELECT * FROM elevators")
    fun getAllElevators(): Flow<List<Elevator>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElevator(elevator: Elevator)

    // Inspections
    @Query("SELECT * FROM inspections ORDER BY date DESC")
    fun getAllInspections(): Flow<List<Inspection>>

    @Query("SELECT * FROM inspections WHERE id = :id")
    suspend fun getInspectionById(id: String): Inspection?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: Inspection)

    @Update
    suspend fun updateInspection(inspection: Inspection)

    // Control Points
    @Query("SELECT * FROM control_points ORDER BY id ASC")
    suspend fun getControlPoints(): List<ControlPoint>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertControlPoints(points: List<ControlPoint>)

    // Inspection Results
    @Query("SELECT * FROM inspection_results WHERE inspectionId = :inspectionId")
    fun getResultsForInspection(inspectionId: String): Flow<List<InspectionResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: InspectionResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<InspectionResult>)
}
