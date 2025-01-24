package sae.iot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import sae.iot.model.Room
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.RoomUiState
import sae.iot.ui.viewmodels.SensorUiState
import androidx.compose.material3.Text
import sae.iot.model.DataSensor


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier) {

    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val roomSelected by homeViewModel.roomSelectedUiState.collectAsStateWithLifecycle()
    val roomUiState = homeViewModel.roomUiState
    val sensorUiState = homeViewModel.sensorsUiState
    var rooms = listOf<Room>()
    when (roomUiState) {
        is RoomUiState.Loading -> {

        }
        is RoomUiState.Success -> {
            rooms = roomUiState.rooms
        }
        is RoomUiState.Error -> {

        }
    }

    var sensors: Map<String, DataSensor> = emptyMap()
    when (sensorUiState) {
        is SensorUiState.Loading -> {

        }
        is SensorUiState.Success -> {
            sensors = sensorUiState.sensors
        }
        is SensorUiState.Error -> {

        }
    }

    Column {
        RoomSelector(
            changeRoomState = { room ->
                homeViewModel.changeRoom(room)
                homeViewModel.getSensors()
            },
            roomSelected = roomSelected,
            rooms = rooms,
            modifier = Modifier.padding(bottom = 20.dp)
        )

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