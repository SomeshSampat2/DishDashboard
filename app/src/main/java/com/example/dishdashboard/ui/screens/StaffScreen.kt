package com.example.dishdashboard.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StaffMember(
    val id: Int,
    val name: String,
    val role: String,
    val status: String,
    val shift: String,
    val phone: String,
    val email: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddStaffDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Mock data
    val staffList = remember {
        listOf(
            StaffMember(1, "Rajesh Kumar", "Chef", "Active", "Morning", "+91 98765 43210", "rajesh@email.com"),
            StaffMember(2, "Priya Sharma", "Server", "Active", "Evening", "+91 98765 43211", "priya@email.com"),
            StaffMember(3, "Amit Patel", "Bartender", "Off-duty", "Night", "+91 98765 43212", "amit@email.com"),
            StaffMember(4, "Neha Gupta", "Host", "Active", "Morning", "+91 98765 43213", "neha@email.com"),
            StaffMember(5, "Suresh Verma", "Kitchen Staff", "On Leave", "Evening", "+91 98765 43214", "suresh@email.com")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Staff",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    // Search Icon
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    // Filter Icon
                    IconButton(onClick = { /* TODO: Implement filter */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    // Add Staff Button
                    IconButton(onClick = { showAddStaffDialog = true }) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Staff")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Stats Cards
            StaffStats(staffList)

            // Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                listOf("All Staff", "Active", "Off-duty", "On Leave").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Staff List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(staffList.filter {
                    when (selectedTab) {
                        1 -> it.status == "Active"
                        2 -> it.status == "Off-duty"
                        3 -> it.status == "On Leave"
                        else -> true
                    }
                }) { staff ->
                    StaffCard(staff)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // Add Staff Dialog
    if (showAddStaffDialog) {
        AddStaffDialog(onDismiss = { showAddStaffDialog = false })
    }
}

@Composable
fun StaffStats(staffList: List<StaffMember>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            StatCard(
                title = "Total Staff",
                value = staffList.size.toString(),
                icon = Icons.Default.People,
                color = Color(0xFF2563EB)
            )
        }
        item {
            StatCard(
                title = "Active Now",
                value = staffList.count { it.status == "Active" }.toString(),
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF10B981)
            )
        }
        item {
            StatCard(
                title = "On Leave",
                value = staffList.count { it.status == "On Leave" }.toString(),
                icon = Icons.Default.EventBusy,
                color = Color(0xFFF59E0B)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffCard(staff: StaffMember) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture/Initial
            Surface(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = staff.name.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Staff Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = staff.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = staff.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "${staff.shift} Shift",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // Status Chip
            Surface(
                modifier = Modifier.padding(start = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = when (staff.status) {
                    "Active" -> Color(0xFF10B981).copy(alpha = 0.1f)
                    "Off-duty" -> Color(0xFF6B7280).copy(alpha = 0.1f)
                    else -> Color(0xFFF59E0B).copy(alpha = 0.1f)
                }
            ) {
                Text(
                    text = staff.status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = when (staff.status) {
                        "Active" -> Color(0xFF10B981)
                        "Off-duty" -> Color(0xFF6B7280)
                        else -> Color(0xFFF59E0B)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStaffDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add New Staff Member",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = shift,
                    onValueChange = { shift = it },
                    label = { Text("Shift") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // TODO: Implement staff addition
                    onDismiss()
                }
            ) {
                Text("Add Staff")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
