package sae.iot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import sae.iot.model.DataSensor
import sae.iot.model.Room
import sae.iot.ui.components.HomeNavigation
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.viewmodels.SensorRoomViewModel
import sae.iot.ui.viewmodels.RoomUiState
import sae.iot.ui.viewmodels.SensorUiState

@Composable
fun RoomScreen(
    navController: NavHostController,
    index: Int,
    modifier: Modifier = Modifier
) {
    val homeViewModel: SensorRoomViewModel =
        viewModel(factory = SensorRoomViewModel.Factory)

    val roomSelected by homeViewModel.roomSelectedUiState.collectAsStateWithLifecycle()
    val roomUiState = homeViewModel.roomUiState
    val sensorUiState = homeViewModel.sensorsUiState
    var rooms = listOf<Room>()

    when (roomUiState) {
        is RoomUiState.Loading -> {}
        is RoomUiState.Success -> {
            rooms = roomUiState.rooms
        }

        is RoomUiState.Error -> {}
    }

    var isLoading: Boolean = false
    var sensors: Map<String, DataSensor> = emptyMap()
    when (sensorUiState) {
        is SensorUiState.Loading -> {
            isLoading = true
        }

        is SensorUiState.Success -> {
            isLoading = false
            sensors = sensorUiState.sensors
        }

        is SensorUiState.Error -> {
            isLoading = true
        }
    }

    Column {
        HomeNavigation(navController, index)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            RoomSelector(
                changeRoomState = { room ->
                    homeViewModel.changeRoom(room)
                    homeViewModel.getSensors()
                },
                roomSelected = roomSelected,
                rooms = rooms,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { homeViewModel.getSensors() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Rafraîchir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 20.dp)
            ) {
                sensors.forEach { (key, sensor) ->
                    LineChart(
                        title = key,
                        measurement = sensor.measurement,
                        listY = sensor.y,
                        listX = sensor.x
                    )
                }
            }
        }
    }
}