package sae.iot.ui.screens

import androidx.annotation.RawRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sae.iot.model.Room
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.viewmodels.ActuatorViewModel
import sae.iot.ui.viewmodels.RoomUiState

@Composable
fun ActuatorScreen(
    actuatorViewModel: ActuatorViewModel,
    modifier: Modifier = Modifier
) {
    val actuatorRoomUiState = actuatorViewModel.roomUiState

    var rooms = listOf<Room>()
    when (actuatorRoomUiState) {
        is RoomUiState.Loading -> {
            Text("charge")
        }

        is RoomUiState.Success -> {
            rooms = actuatorRoomUiState.rooms
        }

        is RoomUiState.Error -> {
            Text("Une erreur est survenue")
        }
    }

    LaunchedEffect(Unit) {
        actuatorViewModel.getRooms()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp)
    ) {
        rooms.forEach { room ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Salle " + room.name.uppercase(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Divider()

                    val isLightOn = remember { mutableStateOf(false) }
                    val buttonShape = RoundedCornerShape(50.dp)

                    // Bouton avec icône et changement de fond
                    Button(
                        onClick = { isLightOn.value = !isLightOn.value },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLightOn.value) Color(0xFFFFFFFF) else Color(
                                0xFF37474F
                            ),
                            contentColor = if (isLightOn.value) Color.Black else Color.White
                        ),
                        shape = buttonShape,
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .then(
                                if (isLightOn.value) {
                                    Modifier.border(
                                        width = 2.dp, // Épaisseur de la bordure
                                        color = Color(0xFF37474F), // Couleur de la bordure
                                        shape = buttonShape // Forme de la bordure
                                    )
                                } else {
                                    Modifier // Pas de bordure si la lumière est éteinte
                                }
                            )
                    ) {
                        Icon(
                            imageVector = if (isLightOn.value) Icons.Default.WbSunny else Icons.Default.NightsStay,
                            contentDescription = if (isLightOn.value) "Lumière ON" else "Lumière OFF",
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isLightOn.value) "Lumière ON" else "Lumière OFF",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}