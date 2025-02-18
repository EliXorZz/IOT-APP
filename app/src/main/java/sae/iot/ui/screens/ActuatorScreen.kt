package sae.iot.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import sae.iot.model.Room
import sae.iot.ui.viewmodels.ActuatorViewModel
import sae.iot.ui.viewmodels.RoomUiState

@Composable
fun ActuatorScreen(
    modifier: Modifier = Modifier
) {
    val actuatorViewModel: ActuatorViewModel =
        viewModel(factory = ActuatorViewModel.Factory)

    val actuatorRoomUiState = actuatorViewModel.roomUiState

    val roomSelected by actuatorViewModel.roomSelectedUiState.collectAsStateWithLifecycle()

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

    val roomsWithLight = listOf("d360", "DTEST")

    Column {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp)
        ) {
            rooms.forEach { room ->
                // Afficher uniquement les salles qui ont un bouton pour la lumière
                if (room.name in roomsWithLight) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = room.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

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
}