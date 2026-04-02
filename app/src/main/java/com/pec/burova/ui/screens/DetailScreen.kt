package com.pec.burova.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import coil.compose.rememberAsyncImagePainter
import com.pec.burova.ui.theme.DarkGray
import com.pec.burova.ui.theme.LightGray
import com.pec.burova.ui.theme.White
import com.pec.burova.ui.viewmodels.StudentViewModel

@Composable
fun DetailScreen(
    viewModel: StudentViewModel,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val student by viewModel.currentStudent.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Студенческий билет",
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.2f),
                    ambientColor = Color.Black.copy(alpha = 0.05f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row inside White Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        onClick = onBack,
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = LightGray),
                        modifier = Modifier.shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.3f),
                            ambientColor = Color.Black.copy(alpha = 0.1f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = com.pec.burova.R.drawable.ic_back_custom),
                            contentDescription = "Back",
                            modifier = Modifier.padding(10.dp).size(24.dp)
                        )
                    }
                    
                    // Avatar in Center of Header Row
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(12.dp),
                                spotColor = Color.Black.copy(alpha = 0.25f),
                                ambientColor = Color.Black.copy(alpha = 0.05f)
                            ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        coil.compose.AsyncImage(
                            model = student?.getAvatarBytes() ?: com.pec.burova.R.drawable.ic_profile_custom,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(com.pec.burova.R.drawable.ic_profile_custom),
                            error = painterResource(com.pec.burova.R.drawable.ic_profile_custom)
                        )
                    }

                    Card(
                        onClick = onNavigateToProfile,
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = LightGray),
                        modifier = Modifier.shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.3f),
                            ambientColor = Color.Black.copy(alpha = 0.1f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = com.pec.burova.R.drawable.ic_profile_custom),
                            contentDescription = "Profile",
                            modifier = Modifier.padding(10.dp).size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info Card (Grey)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = 0.2f),
                            ambientColor = Color.Black.copy(alpha = 0.05f)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = student?.id ?: "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        DetailItem("Образовательная организация", student?.organization ?: "-")
                        DetailItem("ФИО", student?.fullName ?: "-")
                        DetailItem("Дата выдачи", student?.issueDate ?: "-")
                        DetailItem("Направление подготовки", student?.specialty ?: "-")
                        DetailItem("Курс", student?.course ?: "-")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(8.dp),
                            spotColor = Color.Black.copy(alpha = 0.3f),
                            ambientColor = Color.Black.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightGray)
                ) {
                    Text("Местоположение", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ПЭК ГГТУ 2026",
            color = White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label, 
            fontSize = 11.sp, 
            fontWeight = FontWeight.Normal, 
            color = Color.Gray
        )
        Text(
            text = value, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Bold, 
            color = Color.Black
        )
    }
}
