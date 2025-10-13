package com.example.lab04

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab04.ui.theme.Lab04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab04Theme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            if (isLandscape) {
                // Layout horizontal para landscape
                LandscapeLayout()
            } else {
                // Layout vertical para portrait
                PortraitLayout()
            }
        }
    }
}

@Composable
fun PortraitLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PerustearContent()
    }
}

@Composable
fun LandscapeLayout() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PerustearContent()
    }
}

@Composable
fun PerustearContent() {
    var selectedCity by remember { mutableStateOf("Arequipa") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Logo/Título principal
        Text(
            text = "PERUSTEAR",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 42.sp // Usa SP para escalar con accesibilidad
        )

        // Subtítulo
        Text(
            text = "Descubre los Museos del Perú",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card informativa
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Lista de Museos próximos en:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = selectedCity,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón principal - Explorar Museos
        Button(
            onClick = { /* Navegación a lista de museos */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Explorar Museos",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Botón secundario
        OutlinedButton(
            onClick = { /* Cambiar ciudad */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Cambiar Ciudad",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto informativo sobre accesibilidad
        Text(
            text = "Interfaz adaptable a tu configuración de accesibilidad",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

// Preview - Modo Claro (Portrait)
@Preview(
    name = "Light Mode - Portrait",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 800,
    widthDp = 360
)
@Composable
fun MainScreenPreviewLight() {
    Lab04Theme {
        MainScreen()
    }
}

// Preview - Modo Oscuro (Portrait)
@Preview(
    name = "Dark Mode - Portrait",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 800,
    widthDp = 360
)
@Composable
fun MainScreenPreviewDark() {
    Lab04Theme {
        MainScreen()
    }
}

// Preview - Landscape
@Preview(
    name = "Landscape Mode",
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun MainScreenPreviewLandscape() {
    Lab04Theme {
        MainScreen()
    }
}

// Preview - Tablet
@Preview(
    name = "Tablet (10 inches)",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
fun MainScreenPreviewTablet() {
    Lab04Theme {
        MainScreen()
    }
}

// Preview - Teléfono Pequeño
@Preview(
    name = "Small Phone",
    showBackground = true,
    widthDp = 320,
    heightDp = 568
)
@Composable
fun MainScreenPreviewSmallPhone() {
    Lab04Theme {
        MainScreen()
    }
}