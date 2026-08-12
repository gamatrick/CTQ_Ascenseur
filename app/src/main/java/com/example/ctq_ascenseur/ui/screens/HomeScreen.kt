package com.example.ctq_ascenseur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ctq_ascenseur.data.model.Elevator
import com.example.ctq_ascenseur.data.model.Inspection
import com.example.ctq_ascenseur.ui.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onStartInspection: (String) -> Unit
) {
    val elevators by viewModel.elevators.collectAsState()
    val inspections by viewModel.inspections.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CTQ Ascenseurs - A.C.T.I.V", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nouvelle adresse")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Parc d'ascenseurs",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(elevators) { elevator ->
                ElevatorCard(elevator = elevator) {
                    viewModel.startInspection(elevator.id, onStartInspection)
                }
            }

            if (inspections.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Derniers rapports",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(inspections) { inspection ->
                    val elevator = elevators.find { it.id == inspection.elevatorId }
                    InspectionHistoryCard(
                        inspection = inspection,
                        address = elevator?.address ?: "Adresse inconnue",
                        onGeneratePdf = {
                            scope.launch {
                                viewModel.generatePdf(context, inspection)
                            }
                        }
                    )
                }
            }
        }

        if (showAddDialog) {
            AddElevatorDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { address, brand ->
                    showAddDialog = false
                    viewModel.addElevatorAndStartInspection(address, brand, onStartInspection)
                }
            )
        }
    }
}

@Composable
fun InspectionHistoryCard(
    inspection: Inspection,
    address: String,
    onGeneratePdf: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = address, fontWeight = FontWeight.Bold)
                Text(
                    text = "Fait le ${java.text.SimpleDateFormat("dd/MM/yyyy").format(inspection.date)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onGeneratePdf) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = "Générer PDF",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AddElevatorDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var address by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouveau Contrôle") },
        text = {
            Column {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Adresse de l'installation") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marque de l'ascenseur") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (address.isNotBlank()) onConfirm(address, brand) },
                enabled = address.isNotBlank()
            ) {
                Text("Démarrer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Composable
fun ElevatorCard(elevator: Elevator, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = elevator.address, fontWeight = FontWeight.Bold)
                Text(text = "${elevator.brand} - ${elevator.serialNumber}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Add, contentDescription = "Démarrer")
        }
    }
}
