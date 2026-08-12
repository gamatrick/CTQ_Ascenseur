package com.example.ctq_ascenseur.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ctq_ascenseur.data.ControlPointData
import com.example.ctq_ascenseur.data.model.ControlResultStatus
import com.example.ctq_ascenseur.data.model.InspectionResult
import com.example.ctq_ascenseur.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionFormScreen(
    inspectionId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(inspectionId) {
        viewModel.setInspectionId(inspectionId)
    }

    val results by viewModel.currentResults.collectAsState()
    val points = ControlPointData.initialPoints
    val groupedPoints = points.groupBy { it.section }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contrôle Technique") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Terminer", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            groupedPoints.forEach { (section, sectionPoints) ->
                item {
                    SectionHeader(section)
                }
                items(sectionPoints) { point ->
                    val result = results.find { it.controlPointId == point.id }
                    ControlPointItem(
                        pointName = point.name,
                        currentStatus = result?.status ?: ControlResultStatus.CONFORME,
                        onStatusChange = { newStatus ->
                            viewModel.updateResult(
                                InspectionResult(inspectionId, point.id, newStatus, result?.comment, result?.photoPath)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun ControlPointItem(
    pointName: String,
    currentStatus: ControlResultStatus,
    onStatusChange: (ControlResultStatus) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = pointName, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusButton("C", ControlResultStatus.CONFORME, currentStatus == ControlResultStatus.CONFORME) {
                onStatusChange(ControlResultStatus.CONFORME)
            }
            StatusButton("NC", ControlResultStatus.NON_CONFORME, currentStatus == ControlResultStatus.NON_CONFORME) {
                onStatusChange(ControlResultStatus.NON_CONFORME)
            }
            StatusButton("SO", ControlResultStatus.SANS_OBJET, currentStatus == ControlResultStatus.SANS_OBJET) {
                onStatusChange(ControlResultStatus.SANS_OBJET)
            }
            StatusButton("NV", ControlResultStatus.NON_VERIFIABLE, currentStatus == ControlResultStatus.NON_VERIFIABLE) {
                onStatusChange(ControlResultStatus.NON_VERIFIABLE)
            }
        }
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun StatusButton(
    label: String,
    status: ControlResultStatus,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        !isSelected -> MaterialTheme.colorScheme.surfaceVariant
        status == ControlResultStatus.CONFORME -> Color(0xFF4CAF50)
        status == ControlResultStatus.NON_CONFORME -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.primary
    }

    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .width(64.dp)
            .height(48.dp), // Rule: min 48dp for gloves
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
    }
}
