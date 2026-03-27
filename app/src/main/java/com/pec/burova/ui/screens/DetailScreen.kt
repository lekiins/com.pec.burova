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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Image(
                    painter = painterResource(id = com.pec.burova.R.drawable.ic_back_custom),
                    contentDescription = "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Студенческий билет",
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNavigateToProfile) {
                Image(
                    painter = painterResource(id = com.pec.burova.R.drawable.ic_profile_custom),
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.size(100.dp),
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

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                DetailItem("Образовательная организация", student?.organization ?: "-")
                DetailItem("ФИО", student?.fullName ?: "-")
                DetailItem("Дата выдачи", student?.issueDate ?: "-")
                DetailItem("Уровень образования", "Среднее профессиональное")
                DetailItem("Направление подготовки", student?.specialty ?: "-")
                DetailItem("Форма обучения", "Очная")
                DetailItem("Курс", student?.course ?: "-")
                DetailItem("Информация о зачислении", "1230-СК")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToMap,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGray)
        ) {
            Text("Местоположение", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ПЭК ГГТУ 2026",
            color = White,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = value, fontSize = 14.sp, color = Color.Black)
    }
}
