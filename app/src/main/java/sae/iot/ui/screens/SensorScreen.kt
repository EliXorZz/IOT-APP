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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import sae.iot.model.DataSensor
import sae.iot.model.Room
import sae.iot.model.Sensor
import sae.iot.ui.components.HomeNavigation
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.RoomSelector
import sae.iot.ui.components.SensorSelector
import sae.iot.ui.viewmodels.AllSensorUiState
import sae.iot.ui.viewmodels.DataSensorUiState
import sae.iot.ui.viewmodels.RoomUiState
import sae.iot.ui.viewmodels.SensorRoomViewModel
import sae.iot.ui.viewmodels.SensorUiState
import sae.iot.ui.viewmodels.SensorsViewModel

@Composable
fun SensorScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val subMenuIndex by homeViewModel.selectedIndexUiState.collectAsStateWithLifecycle()



    val sensorsViewModel: SensorsViewModel =
        viewModel(factory = SensorsViewModel.Factory)

    val sensorSelected by sensorsViewModel.sensorSelectedUiState.collectAsStateWithLifecycle()

    val allSensorUiState = sensorsViewModel.allSensorUiState
    val dataSensorUiState = sensorsViewModel.dataSensorUiState
    var sensorList: Map<String, Sensor> = emptyMap()

    when (allSensorUiState) {
        is AllSensorUiState.Loading -> {}
        is AllSensorUiState.Success -> {
            sensorList = allSensorUiState.sensors
        }

        is AllSensorUiState.Error -> {}
    }

    var isLoading = false
    var sensorShow: DataSensor? = null
    when (dataSensorUiState) {
        is DataSensorUiState.Loading -> {
            isLoading = true
        }

        is DataSensorUiState.Success -> {
            isLoading = false
            sensorShow = dataSensorUiState.sensor
        }

        is DataSensorUiState.Error -> {
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
            SensorSelector(
                changeSensorState = { sensor ->
                    sensorsViewModel.changeSensor(sensor)
                    sensorsViewModel.getDataSensor()
                },
                sensorSelected = sensorSelected,
                sensorList = sensorList,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { sensorsViewModel.getDataSensor() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Rafraîchir",
                    tint = MaterialTheme.colorScheme.primary
                )
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
                    LineChart(
                        title = sensorSelected!!,
                        measurement = sensorShow?.measurement ?: "N/A",
                        listY = sensorShow?.y ?: emptyList(),
                        listX = sensorShow?.x ?: emptyList()
                    )
                }
            }
        }
    }

}