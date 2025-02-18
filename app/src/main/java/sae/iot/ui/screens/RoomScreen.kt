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
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.RoomUiState
import sae.iot.ui.viewmodels.SensorRoomViewModel
import sae.iot.ui.viewmodels.SensorUiState

@Composable
fun RoomScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val sensorRoomViewModel: SensorRoomViewModel =
        viewModel(factory = SensorRoomViewModel.Factory)

    val subMenuIndex by homeViewModel.selectedIndexUiState.collectAsStateWithLifecycle()
    val roomSelected by sensorRoomViewModel.roomSelectedUiState.collectAsStateWithLifecycle()

    val roomUiState = sensorRoomViewModel.roomUiState
    val sensorUiState = sensorRoomViewModel.sensorsUiState

    var isLoading: Boolean = false

    var rooms = listOf<Room>()
    when (roomUiState) {
        is RoomUiState.Loading -> {}
        is RoomUiState.Success -> {
            rooms = roomUiState.rooms
        }

        is RoomUiState.Error -> {}
    }

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

    Column(modifier = modifier) {
        HomeNavigation(
            selectedIndex = subMenuIndex,
            onSelectedIndex = {
                homeViewModel.setSelectedIndex(navController, it)
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            RoomSelector(
                changeRoomState = { room ->
                    sensorRoomViewModel.changeRoom(room)
                    sensorRoomViewModel.getSensors()
                },
                roomSelected = roomSelected,
                rooms = rooms,
                modifier = Modifier.weight(1f)
            )

            RefreshButton(sensorRoomViewModel)
        }

        if (isLoading) LoadingSpin() else ChartColumn(sensors)
    }
}

@Composable
fun ChartColumn(
    sensors: Map<String, DataSensor>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),

        modifier = modifier
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

@Composable
fun RefreshButton(
    homeViewModel: SensorRoomViewModel,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { homeViewModel.getSensors() },
        modifier = modifier.padding(start = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Rafraîchir",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LoadingSpin(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
}