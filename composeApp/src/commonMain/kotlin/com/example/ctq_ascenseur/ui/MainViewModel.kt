package com.example.ctq_ascenseur.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctq_ascenseur.data.dao.AppDao
import com.example.ctq_ascenseur.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
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
        seedInitialData()
    }

    private fun seedInitialData() {
        viewModelScope.launch {
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
            val id = "INSP-${System.currentTimeMillis()}"
            val inspection = Inspection(id, elevatorId, System.currentTimeMillis(), "Technicien A.C.T.I.V", InspectionStatus.DRAFT)
            dao.insertInspection(inspection)
            
            // Pre-create results for all enabled templates
            val templates = dao.getAllEnabledTemplates()
            val results = templates.map { 
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

    fun addElevatorAndStartInspection(address: String, brand: String, onStarted: (String) -> Unit) {
        viewModelScope.launch {
            val elevatorId = "ELV-${System.currentTimeMillis()}"
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
