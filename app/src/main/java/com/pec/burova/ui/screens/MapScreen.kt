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
import androidx.compose.ui.res.painterResource
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
        Text(
            text = "Местоположение корпусов колледжа",
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
                    text = "10 корпус: Московская область, г. Орехово-Зуево, ул. Иванова, д. 4А",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "12 корпус: Московская область, г. Орехово-Зуево, ул. Ленина, д. 55",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().height(350.dp),
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

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = painterResource(id = com.pec.burova.R.drawable.ic_back_custom),
                        contentDescription = "Back",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
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
