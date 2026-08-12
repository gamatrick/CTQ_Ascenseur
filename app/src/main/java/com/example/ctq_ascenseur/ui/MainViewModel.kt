package com.example.ctq_ascenseur.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ctq_ascenseur.data.ControlPointData
import com.example.ctq_ascenseur.data.dao.AppDao
import com.example.ctq_ascenseur.data.model.*
import com.example.ctq_ascenseur.util.PdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dao: AppDao) : ViewModel() {

    val elevators = dao.getAllElevators()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inspections = dao.getAllInspections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentInspectionId = MutableStateFlow<String?>(null)
    
    val currentResults = _currentInspectionId.flatMapLatest { id ->
        if (id == null) MutableStateFlow(emptyList())
        else dao.getResultsForInspection(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val existingPoints = dao.getControlPoints()
            if (existingPoints.isEmpty()) {
                dao.insertControlPoints(ControlPointData.initialPoints)
            }
            
            // Seed a dummy elevator if none exists for demo
            dao.getAllElevators().collect {
                if (it.isEmpty()) {
                    dao.insertElevator(Elevator("1", "123 Rue de la Tour, Paris", "OTIS", "Gen2", "SN12345", 2015, 630, 1.0, true))
                }
            }
        }
    }

    fun startInspection(elevatorId: String, onStarted: (String) -> Unit) {
        viewModelScope.launch {
            val id = System.currentTimeMillis().toString()
            val inspection = Inspection(id, elevatorId, System.currentTimeMillis(), "Technicien A.C.T.I.V", InspectionStatus.DRAFT)
            dao.insertInspection(inspection)
            
            // Pre-create results for all control points
            val points = dao.getControlPoints()
            val results = points.map { 
                InspectionResult(id, it.id, ControlResultStatus.CONFORME) 
            }
            dao.insertResults(results)
            
            onStarted(id)
        }
    }

    fun setInspectionId(id: String) {
        _currentInspectionId.value = id
    }

    fun updateResult(result: InspectionResult) {
        viewModelScope.launch {
            dao.insertResult(result)
        }
    }

    suspend fun generatePdf(context: Context, inspection: Inspection) {
        val elevator = dao.getAllElevators().first().find { it.id == inspection.elevatorId } ?: return
        val results = dao.getResultsForInspection(inspection.id).first()
        
        val file = PdfGenerator.generateReport(context, elevator, inspection, results)
        if (file != null) {
            android.widget.Toast.makeText(context, "PDF généré : ${file.name}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    fun addElevatorAndStartInspection(address: String, brand: String, onStarted: (String) -> Unit) {
        viewModelScope.launch {
            val elevatorId = System.currentTimeMillis().toString()
            val newElevator = Elevator(
                id = elevatorId,
                address = address,
                brand = brand,
                type = "Standard",
                serialNumber = "SN-${System.currentTimeMillis().toString().takeLast(6)}",
                manufactureYear = 2025,
                loadCapacity = 630,
                speed = 1.0,
                isCeMarked = true
            )
            dao.insertElevator(newElevator)
            startInspection(elevatorId, onStarted)
        }
    }
}

class MainViewModelFactory(private val dao: AppDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
