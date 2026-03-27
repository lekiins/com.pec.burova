package com.pec.burova.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.pec.burova.ui.theme.DarkGray
import com.pec.burova.ui.theme.LightGray
import com.pec.burova.ui.theme.White
import com.pec.burova.ui.viewmodels.StudentViewModel

@Composable
fun ProfileScreen(
    viewModel: StudentViewModel,
    onBack: () -> Unit
) {
    val student by viewModel.currentStudent.collectAsState()
    val uploadStatus by viewModel.uploadStatus.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uploadStatus) {
        uploadStatus?.let {
            if (it == "Success") {
                android.widget.Toast.makeText(context, "Аватар обновлен!", android.widget.Toast.LENGTH_SHORT).show()
            } else if (it.startsWith("Error")) {
                android.widget.Toast.makeText(context, "Ошибка: $it", android.widget.Toast.LENGTH_LONG).show()
            }
            viewModel.clearUploadStatus()
        }
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    val contentType = context.contentResolver.getType(it) ?: "image/png"
                    viewModel.uploadAvatar(bytes, contentType)
                    android.widget.Toast.makeText(context, "Загрузка началась...", android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                android.widget.Toast.makeText(context, "Ошибка при выборе фото", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Image(
                    painter = painterResource(id = com.pec.burova.R.drawable.ic_back_custom),
                    contentDescription = "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Студенческий билет",
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.size(150.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            coil.compose.AsyncImage(
                model = student?.getAvatarBytes() ?: com.pec.burova.R.drawable.ic_profile_custom,
                contentDescription = "Current Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(com.pec.burova.R.drawable.ic_profile_custom),
                error = painterResource(com.pec.burova.R.drawable.ic_profile_custom)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { 
                launcher.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.fillMaxWidth().height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGray)
        ) {
            Text("Загрузить своё фото", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Выберите из галереи",
            color = White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val photos = listOf(
            "https://cataas.com/cat?1",
            "https://cataas.com/cat?2",
            "https://cataas.com/cat?3",
            "https://cataas.com/cat?4",
            "https://cataas.com/cat?5",
            "https://cataas.com/cat?6"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos.size) { index ->
                Card(
                    onClick = { viewModel.uploadAvatarFromUrl(photos[index]) },
                    modifier = Modifier.aspectRatio(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photos[index]),
                        contentDescription = "Avatar Option",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGray)
        ) {
            Text("Готово", color = Color.Black, fontWeight = FontWeight.Bold)
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
