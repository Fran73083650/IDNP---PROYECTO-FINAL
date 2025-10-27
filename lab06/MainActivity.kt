package com.example.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab06.ui.theme.Lab06Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Modelo de datos para lugares turísticos
data class TouristPlace(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val schedule: String,
    val rating: Float,
    var isFavorite: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06Theme {
                TouristGuideApp()
            }
        }
    }
}

@Composable
fun TouristGuideApp() {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
    }

    if (showSplash) {
        TouristSplashScreen()
    } else {
        MainTouristScreen()
    }
}

@Composable
fun TouristSplashScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val locationOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        locationOffset.animateTo(
            targetValue = 1f,
            animationSpec = tween(2000, easing = EaseOutBounce)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF1976D2)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ícono de ubicación animado
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ubicación",
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale)
                    .offset(y = (-50 * (1 - locationOffset.value)).dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Guía Turística",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Descubre lugares increíbles",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Animación de puntos de carga
            LoadingDots()
        }
    }
}

@Composable
fun LoadingDots() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "dot$index")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 200)
                ),
                label = "dot$index"
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alpha))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTouristScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val places = remember { mutableStateListOf<TouristPlace>().apply {
        addAll(getSamplePlaces())
    }}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lugares Turísticos",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Notificaciones */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Place, "Lugares") },
                    label = { Text("Explorar") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, "Favoritos") },
                    label = { Text("Favoritos") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, "Itinerario") },
                    label = { Text("Itinerario") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> ExploreScreen(places, Modifier.padding(padding))
            1 -> FavoritesScreen(places.filter { it.isFavorite }, Modifier.padding(padding))
            2 -> ItineraryScreen(Modifier.padding(padding))
        }
    }
}

@Composable
fun ExploreScreen(places: MutableList<TouristPlace>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // Animación de mapa conceptual
        MapAnimationHeader()

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places) { place ->
                TouristPlaceCard(
                    place = place,
                    onFavoriteClick = {
                        val index = places.indexOf(place)
                        places[index] = place.copy(isFavorite = !place.isFavorite)
                    }
                )
            }
        }
    }
}

@Composable
fun MapAnimationHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "map")
    val markerScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "marker"
    )

    val pathProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "path"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Dibujar ruta turística animada
            val path = Path().apply {
                moveTo(width * 0.1f, height * 0.5f)
                cubicTo(
                    width * 0.3f, height * 0.2f,
                    width * 0.5f, height * 0.8f,
                    width * 0.7f, height * 0.4f
                )
                lineTo(width * 0.9f, height * 0.6f)
            }

            drawPath(
                path = path,
                color = Color(0xFF2196F3),
                style = Stroke(
                    width = 8f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                        floatArrayOf(20f, 10f),
                        phase = pathProgress * 100f
                    )
                )
            )

            // Marcadores de lugares
            val markers = listOf(
                Offset(width * 0.1f, height * 0.5f),
                Offset(width * 0.4f, height * 0.35f),
                Offset(width * 0.7f, height * 0.4f),
                Offset(width * 0.9f, height * 0.6f)
            )

            markers.forEachIndexed { index, offset ->
                val scale = if (index == (pathProgress * markers.size).toInt() % markers.size) {
                    markerScale
                } else {
                    1f
                }

                drawCircle(
                    color = Color(0xFF1976D2),
                    radius = 15f * scale,
                    center = offset
                )
                drawCircle(
                    color = Color.White,
                    radius = 8f * scale,
                    center = offset
                )
            }
        }
    }
}

@Composable
fun TouristPlaceCard(place: TouristPlace, onFavoriteClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val cardScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale.value)
            .clickable {
                scope.launch {
                    cardScale.animateTo(0.95f, tween(100))
                    cardScale.animateTo(1f, tween(100))
                }
                isExpanded = !isExpanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when(place.category) {
                            "Histórico" -> Icons.Default.Home
                            "Natural" -> Icons.Default.Place
                            "Cultural" -> Icons.Default.AccountCircle
                            else -> Icons.Default.Place
                        },
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = place.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = place.category,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (place.isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = place.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = place.schedule,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < place.rating) Icons.Default.Star else Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (index < place.rating) Color(0xFFFFC107) else Color.LightGray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${place.rating}/5",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(favorites: List<TouristPlace>, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        if (favorites.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No tienes favoritos aún",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites) { place ->
                    TouristPlaceCard(place = place, onFavoriteClick = {})
                }
            }
        }
    }
}

@Composable
fun ItineraryScreen(modifier: Modifier = Modifier) {
    val timeProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        timeProgress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Planifica tu visita",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Crea itinerarios personalizados",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animación de cronómetro/temporizador
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = 80f

                // Círculo de fondo
                drawCircle(
                    color = Color(0xFFE0E0E0),
                    radius = radius,
                    center = Offset(centerX, centerY)
                )

                // Progreso circular animado
                drawArc(
                    color = Color(0xFF4CAF50),
                    startAngle = -90f,
                    sweepAngle = 360f * timeProgress.value,
                    useCenter = false,
                    style = Stroke(width = 12f),
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )

                // Manecilla del reloj
                val angle = timeProgress.value * 360f - 90f
                val radian = Math.toRadians(angle.toDouble())
                val lineEndX = centerX + (radius * 0.7f * Math.cos(radian)).toFloat()
                val lineEndY = centerY + (radius * 0.7f * Math.sin(radian)).toFloat()

                drawLine(
                    color = Color(0xFF1976D2),
                    start = Offset(centerX, centerY),
                    end = Offset(lineEndX, lineEndY),
                    strokeWidth = 4f
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Próximamente: Sistema de itinerarios",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

fun getSamplePlaces(): List<TouristPlace> {
    return listOf(
        TouristPlace(
            1,
            "Plaza de Armas",
            "Centro histórico de la ciudad con arquitectura colonial. Punto de encuentro y eventos culturales.",
            "Histórico",
            "Lun-Dom: 24 horas",
            4.5f
        ),
        TouristPlace(
            2,
            "Museo Nacional",
            "Colección de arte precolombino y colonial. Exposiciones temporales de artistas locales.",
            "Cultural",
            "Mar-Dom: 9:00-18:00",
            4.2f
        ),
        TouristPlace(
            3,
            "Mirador del Valle",
            "Vista panorámica de la ciudad y montañas circundantes. Ideal para fotografía.",
            "Natural",
            "Lun-Dom: 6:00-20:00",
            4.8f
        ),
        TouristPlace(
            4,
            "Catedral Metropolitana",
            "Iglesia del siglo XVIII con arte religioso y arquitectura barroca.",
            "Histórico",
            "Lun-Sáb: 8:00-19:00",
            4.6f
        ),
        TouristPlace(
            5,
            "Parque Ecológico",
            "Área verde con senderos, laguna y actividades al aire libre para toda la familia.",
            "Natural",
            "Lun-Dom: 7:00-19:00",
            4.4f
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    Lab06Theme {
        TouristGuideApp()
    }
}