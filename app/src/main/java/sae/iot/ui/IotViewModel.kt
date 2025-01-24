package sae.iot.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.IotApplication
import sae.iot.model.DataSensor
import sae.iot.data.SensorsRepository
import java.io.IOException

sealed interface SensorUiState {
    data class Success(val sensor: DataSensor): SensorUiState
    object Error: SensorUiState
    object Loading: SensorUiState
}

class IotViewModel(private val sensorRepository: SensorsRepository) : ViewModel() {
    var sensorsUiState: SensorUiState by mutableStateOf(SensorUiState.Loading)
        private set

    init {
        getSensor()
    }

    fun getSensor() {
        viewModelScope.launch {
            sensorsUiState = SensorUiState.Loading
            sensorsUiState = try {
                val sensor: DataSensor = sensorRepository.getDataSensor("d251_air_temperature")
                SensorUiState.Success(
                    sensor = sensor
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                SensorUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                SensorUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val sensorsRepository = application.container.SensorsRepository
                IotViewModel(sensorRepository = sensorsRepository)
            }
        }
    }
}