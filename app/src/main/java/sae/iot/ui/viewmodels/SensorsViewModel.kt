package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.IotApplication
import sae.iot.model.DataSensor
import sae.iot.data.SensorsRepository
import sae.iot.model.Sensor
import java.io.IOException
import kotlin.math.log

sealed interface AllSensorUiState {
    data class Success(val sensors: Map<String, Sensor>) : AllSensorUiState
    object Error : AllSensorUiState
    object Loading : AllSensorUiState
}

sealed interface DataSensorUiState {
    data class Success(val sensor: DataSensor) : DataSensorUiState
    object Error : DataSensorUiState
    object Loading : DataSensorUiState
}

class SensorsViewModel(
    private val sensorRepository: SensorsRepository,
) : ViewModel() {

    private val _sensorSelectedUiState: MutableStateFlow<String?> = MutableStateFlow(null)

    val sensorSelectedUiState = _sensorSelectedUiState.asStateFlow()

    var allSensorUiState: AllSensorUiState by mutableStateOf(AllSensorUiState.Loading)
        private set

    var dataSensorUiState: DataSensorUiState by mutableStateOf(DataSensorUiState.Loading)
        private set

    init {
        getAllSensor()
    }

    fun changeSensor(sensor: String? = "d251_air_temperature") {
        _sensorSelectedUiState.update {
            sensor
        }
        getDataSensor()
    }

    fun getAllSensor() {
        viewModelScope.launch {
            allSensorUiState = AllSensorUiState.Loading
            allSensorUiState = try {
                val sensorNames = sensorRepository.getSensorsName()
                sensorNames.keys.firstOrNull()?.let { firstSensor ->
                    _sensorSelectedUiState.update { firstSensor }
                }
                AllSensorUiState.Success(
                    sensors = sensorNames
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                AllSensorUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                AllSensorUiState.Error
            }
        }
    }

    fun getDataSensor() {
        Log.v("here1", "here1")
        viewModelScope.launch {
            Log.v("here2", "here2")
            dataSensorUiState = DataSensorUiState.Loading
            dataSensorUiState = try {
                val sensor = sensorRepository.getDataSensor(sensorSelectedUiState.value!!)
                DataSensorUiState.Success(
                    sensor = sensor
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                DataSensorUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                DataSensorUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val sensorsRepository = application.container.SensorsRepository
                SensorsViewModel(
                    sensorRepository = sensorsRepository,
                )
            }
        }
    }
}