package sae.iot.ui

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
    data class Success(val sensor: String): SensorUiState
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
                val listResult = sensorRepository.getDataSensor("d251_air_temperature")
                SensorUiState.Success(
                    "Success: api working"
                )
            } catch (e: IOException) {
                SensorUiState.Error
            } catch (e: HttpException) {
                SensorUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val sensorRepository = application.container.MetricsRepository
                IotViewModel(sensorRepository = sensorRepository)
            }
        }
    }
}