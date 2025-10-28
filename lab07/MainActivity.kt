package com.example.lab07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lab07.ui.theme.Lab07Theme

// ==================== MODELO DE DATOS ====================
data class TouristDestination(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val location: String,
    val rating: Float,
    val schedule: String,
    val price: String,
    val visitorsCount: String,
    val latitude: Double,
    val longitude: Double,
    var isFavorite: Boolean = false,
    val avatar: ImageVector = Icons.Default.Place
)

// ==================== ACTIVITY PRINCIPAL ====================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab07Theme {
                TouristGuideApp()
            }
        }
    }
}

// ==================== APP PRINCIPAL ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristGuideApp() {
    var searchQuery by remember { mutableStateOf("") }
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var selectedDestination by remember { mutableStateOf<TouristDestination?>(null) }
    val destinations = remember { mutableStateListOf<TouristDestination>().apply { addAll(getSampleDestinations()) } }

    val filteredDestinations = destinations.filter { destination ->
        val matchesSearch = destination.name.contains(searchQuery, ignoreCase = true) ||
                destination.category.contains(searchQuery, ignoreCase = true) ||
                destination.location.contains(searchQuery, ignoreCase = true)
        val matchesFavorite = !showFavoritesOnly || destination.isFavorite
        matchesSearch && matchesFavorite
    }

    if (selectedDestination != null) {
        DestinationDetailScreen(
            destination = selectedDestination!!,
            onBackClick = { selectedDestination = null },
            onFavoriteClick = {
                val index = destinations.indexOfFirst { it.id == selectedDestination!!.id }
                if (index != -1) {
                    destinations[index] = destinations[index].copy(isFavorite = !destinations[index].isFavorite)
                    selectedDestination = destinations[index]
                }
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Guía Turística Perú",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        IconButton(onClick = { showFavoritesOnly = !showFavoritesOnly }) {
                            Icon(
                                if (showFavoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Mostrar favoritos",
                                tint = if (showFavoritesOnly) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("3")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Barra de búsqueda
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar destinos...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )

                // Estadísticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        value = destinations.size.toString(),
                        label = "Destinos",
                        icon = Icons.Default.Place,
                        color = MaterialTheme.colorScheme.primary
                    )
                    StatCard(
                        value = destinations.count { it.isFavorite }.toString(),
                        label = "Favoritos",
                        icon = Icons.Default.Favorite,
                        color = Color(0xFFE91E63)
                    )
                    StatCard(
                        value = "24/7",
                        label = "Disponible",
                        icon = Icons.Default.Info,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                // Lista de destinos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredDestinations, key = { it.id }) { destination ->
                        DestinationCard(
                            destination = destination,
                            onFavoriteClick = {
                                val index = destinations.indexOfFirst { it.id == destination.id }
                                if (index != -1) {
                                    destinations[index] = destinations[index].copy(
                                        isFavorite = !destinations[index].isFavorite
                                    )
                                }
                            },
                            onCardClick = { selectedDestination = destination }
                        )
                    }

                    if (filteredDestinations.isEmpty()) {
                        item {
                            EmptyState(
                                message = if (showFavoritesOnly)
                                    "No tienes destinos favoritos"
                                else
                                    "No se encontraron destinos"
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== COMPONENTES UI ====================
@Composable
fun StatCard(value: String, label: String, icon: ImageVector, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun DestinationCard(
    destination: TouristDestination,
    onFavoriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Imagen/Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        )
                ) {
                    // Avatar circular en el centro
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    ) {
                        Icon(
                            destination.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Overlay con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 80f
                            )
                        )
                )

                // Badge de categoría
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        destination.category,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón favorito
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        if (destination.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (destination.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Nombre
                Text(
                    destination.name,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Información
            Column(modifier = Modifier.padding(16.dp)) {
                // Rating y visitantes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(destination.rating.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("/5.0", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${destination.visitorsCount}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Text(
                    destination.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ubicación
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(destination.location, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horario y precio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoChip(Icons.Default.DateRange, destination.schedule)
                    InfoChip(Icons.Default.AttachMoney, destination.price, MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String, color: Color = MaterialTheme.colorScheme.secondary) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = color)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.bodySmall, color = color, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

// ==================== PANTALLA DE DETALLES ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    destination: TouristDestination,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Destino") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            if (destination.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (destination.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                // Header con imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        )
                ) {
                    Icon(
                        destination.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f)
                    )

                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            destination.category,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Título y ubicación
                    Text(
                        destination.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(destination.location, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Rating destacado
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(destination.rating.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(" / 5.0", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("${destination.visitorsCount} visitantes", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Descripción
                    Text("Descripción", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(destination.description, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Información
                    DetailInfoCard(
                        title = "Horario",
                        icon = Icons.Default.AccessTime,
                        content = destination.schedule
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoCard(
                        title = "Precio de entrada",
                        icon = Icons.Default.AttachMoney,
                        content = destination.price
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoCard(
                        title = "Coordenadas GPS",
                        icon = Icons.Default.MyLocation,
                        content = "Lat: ${destination.latitude}\nLon: ${destination.longitude}"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botones de acción
                    Button(
                        onClick = { /* Navegar */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DirectionsWalk, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cómo llegar")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoCard(title: String, icon: ImageVector, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(content, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// ==================== DATOS DE EJEMPLO (25 destinos) ====================
fun getSampleDestinations(): List<TouristDestination> {
    return listOf(
        TouristDestination(1, "Machu Picchu", "Histórico", "Ciudadela inca del siglo XV en los Andes", "Cusco", 4.9f, "6:00-17:30", "S/ 152", "5.2M", -13.1631, -72.5450, avatar = Icons.Default.Home),
        TouristDestination(2, "Plaza de Armas", "Cultural", "Centro histórico colonial con arquitectura impresionante", "Cusco", 4.7f, "24 horas", "Gratis", "3.8M", -13.5170, -71.9785, avatar = Icons.Default.LocationCity),
        TouristDestination(3, "Lago Titicaca", "Natural", "Lago navegable más alto del mundo", "Puno", 4.8f, "24 horas", "S/ 25", "2.1M", -15.8422, -69.3658, avatar = Icons.Default.Landscape),
        TouristDestination(4, "Líneas de Nazca", "Misterioso", "Geoglifos milenarios visibles desde el aire", "Ica", 4.6f, "9:00-16:00", "S/ 280", "890K", -14.7390, -75.1300, avatar = Icons.Default.Visibility),
        TouristDestination(5, "Cañón del Colca", "Aventura", "Uno de los cañones más profundos del mundo", "Arequipa", 4.8f, "6:00-18:00", "S/ 70", "1.5M", -15.6186, -71.8786, avatar = Icons.Default.Terrain),
        TouristDestination(6, "Centro Histórico Lima", "Patrimonio", "Patrimonio de la Humanidad UNESCO", "Lima", 4.5f, "24 horas", "Gratis", "8.5M", -12.0464, -77.0428, avatar = Icons.Default.LocationCity),
        TouristDestination(7, "Huacachina", "Oasis", "Oasis natural rodeado de dunas", "Ica", 4.7f, "24 horas", "S/ 15", "650K", -14.0874, -75.7630, avatar = Icons.Default.WbSunny),
        TouristDestination(8, "Sacsayhuamán", "Arqueológico", "Complejo ceremonial con enormes bloques de piedra", "Cusco", 4.7f, "7:00-18:00", "S/ 70", "1.2M", -13.5089, -71.9816, avatar = Icons.Default.Apartment),
        TouristDestination(9, "Reserva de Paracas", "Ecológico", "Reserva natural con fauna marina", "Ica", 4.6f, "8:00-17:00", "S/ 11", "780K", -13.8347, -76.2506, avatar = Icons.Default.Nature),
        TouristDestination(10, "Valle Sagrado", "Histórico", "Valle con importantes sitios arqueológicos incas", "Cusco", 4.8f, "8:00-17:00", "S/ 70", "2.3M", -13.3117, -72.0775, avatar = Icons.Default.Terrain),
        TouristDestination(11, "Santa Catalina", "Colonial", "Monasterio del siglo XVI", "Arequipa", 4.6f, "9:00-17:00", "S/ 40", "450K", -16.3967, -71.5372, avatar = Icons.Default.Home),
        TouristDestination(12, "Kuélap", "Fortaleza", "Ciudadela de la cultura Chachapoyas", "Amazonas", 4.7f, "8:00-16:00", "S/ 20", "180K", -6.4200, -77.9244, avatar = Icons.Default.Security),
        TouristDestination(13, "Circuito del Agua", "Entretenimiento", "Parque con 13 fuentes iluminadas", "Lima", 4.4f, "15:00-22:30", "S/ 4", "2.1M", -12.0708, -77.0600, avatar = Icons.Default.Waves),
        TouristDestination(14, "Chan Chan", "Arqueológico", "Ciudad de barro más grande de América", "La Libertad", 4.5f, "9:00-16:00", "S/ 10", "320K", -8.1061, -79.0753, avatar = Icons.Default.AccountBalance),
        TouristDestination(15, "Islas Ballestas", "Fauna", "Islas con lobos marinos y aves", "Ica", 4.6f, "8:00-13:00", "S/ 50", "890K", -13.7461, -76.4061, avatar = Icons.Default.DirectionsBoat),
        TouristDestination(16, "Baños del Inca", "Termal", "Aguas termales curativas", "Cajamarca", 4.5f, "5:00-20:00", "S/ 8", "280K", -7.1670, -78.4694, avatar = Icons.Default.Spa),
        TouristDestination(17, "Catedral de Lima", "Religioso", "Basílica primada del Perú", "Lima", 4.6f, "9:00-17:00", "S/ 15", "1.8M", -12.0464, -77.0300, avatar = Icons.Default.Domain),
        TouristDestination(18, "Laguna 69", "Montaña", "Laguna turquesa a 4,600 msnm", "Áncash", 4.9f, "5:00-15:00", "S/ 30", "95K", -9.0247, -77.6019, avatar = Icons.Default.Landscape),
        TouristDestination(19, "Máncora", "Playa", "Mejores playas del norte", "Piura", 4.5f, "24 horas", "Gratis", "780K", -4.1053, -81.0453, avatar = Icons.Default.WbSunny),
        TouristDestination(20, "Parque Leyendas", "Zoológico", "Zoo y museo arqueológico", "Lima", 4.3f, "9:00-17:00", "S/ 15", "1.5M", -12.0692, -77.0733, avatar = Icons.Default.Pets),
        TouristDestination(21, "Choquequirao", "Perdido", "Hermana sagrada de Machu Picchu", "Cusco", 4.8f, "6:00-16:00", "S/ 60", "35K", -13.5331, -72.8983, avatar = Icons.Default.Explore),
        TouristDestination(22, "Museo Larco", "Arte", "Colección de arte precolombino", "Lima", 4.7f, "10:00-19:00", "S/ 35", "420K", -12.0692, -77.0700, avatar = Icons.Default.Palette),
        TouristDestination(23, "Huayhuash", "Trekking", "Circuito más bello del mundo", "Áncash", 4.9f, "Todo el día", "S/ 200", "12K", -10.2667, -76.9000, avatar = Icons.Default.DirectionsWalk),
        TouristDestination(24, "Gocta", "Cascada", "Una de las cataratas más altas del mundo", "Amazonas", 4.7f, "6:00-16:00", "S/ 10", "85K", -5.9147, -77.8844, avatar = Icons.Default.Waves),
        TouristDestination(25, "Parque Kennedy", "Urbano", "Parque emblemático de Miraflores", "Lima", 4.4f, "24 horas", "Gratis", "4.2M", -12.1203, -77.0300, avatar = Icons.Default.Park)
    )
}