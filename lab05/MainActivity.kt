package com.example.lab05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab05.ui.theme.Lab05Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab05Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormularioTuristicoApp()
                }
            }
        }
    }
}

@Composable
fun FormularioTuristicoApp(modifier: Modifier = Modifier) {
    var nombreCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    var tipoVisitante by remember { mutableStateOf("Turista") }
    var notificacionesEventos by remember { mutableStateOf(true) }
    var notificacionesOfertas by remember { mutableStateOf(false) }
    var modoOffline by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    // Colores temáticos para turismo
    val turquoisaColor = Color(0xFF00BCD4)
    val naranjaColor = Color(0xFFFF9800)
    val verdeColor = Color(0xFF4CAF50)
    val azulCieloColor = Color(0xFF03A9F4)
    val fondoColor = Color(0xFFF0F8FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoColor)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Encabezado con color turístico
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = turquoisaColor
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🌍 Guía Turística",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Descubre los mejores destinos",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Campo: Nombre Completo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = " Nombre Completo:",
                        fontWeight = FontWeight.Medium,
                        color = turquoisaColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = nombreCompleto,
                        onValueChange = { nombreCompleto = it },
                        label = { Text("Ingrese su nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = turquoisaColor,
                            cursorColor = turquoisaColor
                        )
                    )
                }
            }

            // Campo: Email
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Correo Electrónico:",
                        fontWeight = FontWeight.Medium,
                        color = turquoisaColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("ejemplo@correo.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = turquoisaColor,
                            cursorColor = turquoisaColor
                        )
                    )
                }
            }

            // Campo: Contraseña
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contraseña:",
                        fontWeight = FontWeight.Medium,
                        color = turquoisaColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Ingrese su contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = turquoisaColor,
                            cursorColor = turquoisaColor
                        )
                    )
                }
            }

            // Campo: Nacionalidad
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nacionalidad:",
                        fontWeight = FontWeight.Medium,
                        color = turquoisaColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = nacionalidad,
                        onValueChange = { nacionalidad = it },
                        label = { Text("País de origen") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = turquoisaColor,
                            cursorColor = turquoisaColor
                        )
                    )
                }
            }

            // Sección: Tipo de Visitante
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tipo de Visitante:",
                        fontWeight = FontWeight.Medium,
                        color = naranjaColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoVisitante == "Turista",
                            onClick = { tipoVisitante = "Turista" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = naranjaColor
                            )
                        )
                        Text("Turista", modifier = Modifier.padding(end = 16.dp))

                        RadioButton(
                            selected = tipoVisitante == "Residente",
                            onClick = { tipoVisitante = "Residente" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = naranjaColor
                            )
                        )
                        Text("Residente")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoVisitante == "Estudiante",
                            onClick = { tipoVisitante = "Estudiante" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = naranjaColor
                            )
                        )
                        Text("Estudiante", modifier = Modifier.padding(end = 16.dp))

                        RadioButton(
                            selected = tipoVisitante == "Otro",
                            onClick = { tipoVisitante = "Otro" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = naranjaColor
                            )
                        )
                        Text("Otro")
                    }
                }
            }

            // Sección: Preferencias de Notificaciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferencias de Notificaciones:",
                        fontWeight = FontWeight.Medium,
                        color = azulCieloColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = notificacionesEventos,
                            onCheckedChange = { notificacionesEventos = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = azulCieloColor,
                                checkedTrackColor = azulCieloColor.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Eventos locales")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = notificacionesOfertas,
                            onCheckedChange = { notificacionesOfertas = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = azulCieloColor,
                                checkedTrackColor = azulCieloColor.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Ofertas y promociones")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = modoOffline,
                            onCheckedChange = { modoOffline = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = verdeColor,
                                checkedTrackColor = verdeColor.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Modo offline (descargas)")
                    }
                }
            }

            // Checkbox: Términos y Condiciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Checkbox(
                        checked = aceptaTerminos,
                        onCheckedChange = { aceptaTerminos = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = verdeColor
                        )
                    )
                    Text("Acepto los términos y condiciones de uso")
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        nombreCompleto = ""
                        email = ""
                        password = ""
                        nacionalidad = ""
                        tipoVisitante = "Turista"
                        notificacionesEventos = true
                        notificacionesOfertas = false
                        modoOffline = false
                        aceptaTerminos = false
                        mensaje = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9E9E9E)
                    )
                ) {
                    Text(" Limpiar", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (nombreCompleto.isBlank() || email.isBlank() || password.isBlank()) {
                            mensaje = "Por favor, complete todos los campos obligatorios"
                        } else if (!aceptaTerminos) {
                            mensaje = "Debe aceptar los términos y condiciones"
                        } else {
                            mensaje = "¡Registro exitoso! Bienvenido $nombreCompleto"
                            println("=== FORMULARIO ENVIADO ===")
                            println("Nombre: $nombreCompleto")
                            println("Email: $email")
                            println("Nacionalidad: $nacionalidad")
                            println("Tipo de visitante: $tipoVisitante")
                            println("Notificaciones eventos: $notificacionesEventos")
                            println("Notificaciones ofertas: $notificacionesOfertas")
                            println("Modo offline: $modoOffline")
                            println("Términos aceptados: $aceptaTerminos")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = aceptaTerminos,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = verdeColor,
                        disabledContainerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Text("✅ Registrar", fontSize = 16.sp)
                }
            }

            // Mensaje de resultado
            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (mensaje.contains("exitoso"))
                            verdeColor.copy(alpha = 0.2f)
                        else
                            Color(0xFFFFCDD2)
                    )
                ) {
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(16.dp),
                        color = if (mensaje.contains("exitoso"))
                            Color(0xFF1B5E20)
                        else
                            Color(0xFFB71C1C),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FormularioTuristicoPreview() {
    Lab05Theme {
        Surface {
            FormularioTuristicoApp()
        }
    }
}