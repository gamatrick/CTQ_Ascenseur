package com.example.ctq_ascenseur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ctq_ascenseur.data.model.Inspection
import com.example.ctq_ascenseur.ui.MainViewModel
import com.example.ctq_ascenseur.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onStartNewReport: () -> Unit,
    onViewReport: (String) -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val inspections by viewModel.inspections.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CTQ Ascenseur", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = onStartNewReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("COMMENCER UN RAPPORT", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Anciens Rapports",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(inspections) { inspection ->
                    InspectionCard(
                        inspection = inspection,
                        onClick = { onViewReport(inspection.id) },
                        onExport = { /* Export Logic */ }
                    )
                }
            }
        }
    }
}

@Composable
fun InspectionCard(
    inspection: Inspection,
    onClick: () -> Unit,
    onExport: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Rapport #${inspection.id.takeLast(6)}", fontWeight = FontWeight.Bold)
                Text(inspection.technicianName, style = MaterialTheme.typography.bodySmall)
                Text(
                    "ID: ${inspection.id}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            IconButton(
                onClick = onExport,
                modifier = Modifier.size(AppTheme.touchTargetSize)
            ) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = "Exporter PDF",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
