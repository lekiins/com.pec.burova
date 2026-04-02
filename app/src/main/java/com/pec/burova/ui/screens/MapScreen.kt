package com.pec.burova.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.pec.burova.ui.theme.DarkGray
import com.pec.burova.ui.theme.LightGray
import com.pec.burova.ui.theme.White

@Composable
fun MapScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var markersAdded by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        MapKitFactory.getInstance().onStart()
        onDispose {
            MapKitFactory.getInstance().onStop()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = com.pec.burova.R.drawable.logo_pek),
            contentDescription = "College Logo",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Студенческий билет",
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append("10 корпус: ")
                        }
                        append("Московская область, г. Орехово-Зуево, ул. Иванова, д. 4А")
                    },
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append("12 корпус: ")
                        }
                        append("Московская область, г. Орехово-Зуево, ул. Ленина, д. 55")
                    },
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                ) {
                    AndroidView(
                        factory = { 
                            val center = Point(55.806, 38.966)
                            MapView(context).apply {
                                onStart()
                                mapWindow.map.move(
                                    CameraPosition(center, 13.5f, 0.0f, 0.0f)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { mapView ->
                            mapView.onStart()
                            if (!markersAdded) {
                                try {
                                    val map = mapView.mapWindow.map
                                    map.mapObjects.clear()
                                    
                                    val pek10Location = Point(55.8113, 38.9551)
                                    val pek12Location = Point(55.8010, 38.9785)
                                    
                                    // 10 Corps (Circle Marker)
                                    map.mapObjects.addCircle(Circle(pek10Location, 25f)).apply {
                                        strokeColor = Color.Red.toArgb()
                                        strokeWidth = 3f
                                        fillColor = Color.Red.copy(alpha = 0.3f).toArgb()
                                        zIndex = 100f
                                    }
                                    map.mapObjects.addCircle(Circle(pek10Location, 5f)).apply {
                                        fillColor = Color.Red.toArgb()
                                        zIndex = 101f
                                    }

                                    // 12 Corps (Circle Marker)
                                    map.mapObjects.addCircle(Circle(pek12Location, 25f)).apply {
                                        strokeColor = Color.Blue.toArgb()
                                        strokeWidth = 3f
                                        fillColor = Color.Blue.copy(alpha = 0.3f).toArgb()
                                        zIndex = 100f
                                    }
                                    map.mapObjects.addCircle(Circle(pek12Location, 5f)).apply {
                                        fillColor = Color.Blue.toArgb()
                                        zIndex = 101f
                                    }
                                    
                                    markersAdded = true
                                } catch (e: Exception) {
                                    markersAdded = true
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightGray),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("На главную", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ПЭК ГГТУ 2026",
            color = White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
