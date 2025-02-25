package sae.iot.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import sae.iot.model.DataSensor
import sae.iot.model.Sensor
import sae.iot.ui.components.CurrentChart
import sae.iot.ui.components.DiscomfortAlert
import sae.iot.ui.components.HomeNavigation
import sae.iot.ui.components.LineChart
import sae.iot.ui.components.LoadingSpin
import sae.iot.ui.components.SensorSelector
import sae.iot.ui.components.SwitchViewButton
import sae.iot.ui.viewmodels.AllSensorUiState
import sae.iot.ui.viewmodels.DataSensorUiState
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.SensorsViewModel
import sae.iot.ui.viewmodels.ViewType

@Composable
fun SensorScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    sensorsViewModel: SensorsViewModel,
    modifier: Modifier = Modifier
) {
    val subMenuIndex by homeViewModel.selectedIndexUiState.collectAsStateWithLifecycle()
    val viewType by homeViewModel.viewTypeUiState.collectAsStateWithLifecycle()

    val sensorSelected by sensorsViewModel.sensorSelectedUiState.collectAsStateWithLifecycle()

    val allSensorUiState = sensorsViewModel.allSensorUiState
    val dataSensorUiState = sensorsViewModel.dataSensorUiState
    var sensorMap: Map<String, Sensor> = emptyMap()

    when (allSensorUiState) {
        is AllSensorUiState.Loading -> {}
        is AllSensorUiState.Success -> {
            sensorMap = allSensorUiState.sensors
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

    LaunchedEffect(Unit) {
        sensorsViewModel.fresh()
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
                sensorMap = sensorMap,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier.padding(start = 15.dp)
            ) {
                SwitchViewButton(homeViewModel, viewType)
                RefreshButton(sensorsViewModel)
            }
        }

        if (isLoading) LoadingSpin() else ChartSensor(
            sensorId = sensorSelected!!,
            sensor = sensorShow!!,
            type = viewType
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun ChartSensor(
    sensorId: String,
    sensor: DataSensor,
    type: ViewType,
) {
    val typeSensor = sensorId.split("_")[1]

    val label: String = when(typeSensor) {
        "air" -> "Température"
        "dew" -> "Point de rosée"
        "co2" -> "Niveau de CO2"
        "humidity" -> "Humidité"
        "illuminance" -> "Luminosité"
        "ultraviolet" -> "Niveau UV"
        "smoke" -> "Densité de fumée"
        "loudness" -> "Niveau sonore"
        "volatile" -> "COV (Composés Organiques Volatils)"
        "binary" -> "Détecteur de mouvement"
        else -> "Error"
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp)
    ) {
        if (type == ViewType.CURRENT) {
            CurrentChart(
                title = label,
                measurement = sensor.measurement,
                listY = sensor.y,
                discomfort = sensor.discomfort
            )
        } else {
            LineChart(
                title = label,
                measurement = sensor.measurement,
                listY = sensor.y,
                listX = sensor.x,
                discomfort = sensor.discomfort
            )
        }
    }
}

@Composable
private fun RefreshButton(
    sensorsViewModel: SensorsViewModel,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { sensorsViewModel.getDataSensor() },
        modifier = modifier.padding(start = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Rafraîchir",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
