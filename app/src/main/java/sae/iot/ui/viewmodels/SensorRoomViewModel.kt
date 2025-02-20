package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.IotApplication
import sae.iot.data.RoomsRepository
import sae.iot.model.DataSensor
import sae.iot.data.SensorsRepository
import java.io.IOException

sealed interface SensorUiState {
    data class Success(val sensors: Map<String, DataSensor>) : SensorUiState
    object Error : SensorUiState
    object Loading : SensorUiState
}

class SensorRoomViewModel(
    private val sensorRepository: SensorsRepository,
    private val roomsRepository: RoomsRepository
) : RoomViewModel(roomsRepository) {

    var sensorsUiState: SensorUiState by mutableStateOf(SensorUiState.Loading)
        private set

    fun getSensors() {
        viewModelScope.launch {
            sensorsUiState = SensorUiState.Loading
            sensorsUiState = try {
                val sensors = sensorRepository.getDataSensorsByRoom(super.roomSelectedUiState.value!!)
                SensorUiState.Success(
                    sensors = sensors
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
                val roomsRepository = application.container.RoomsRepository
                SensorRoomViewModel(
                    sensorRepository = sensorsRepository,
                    roomsRepository = roomsRepository
                )
            }
        }
    }

    fun refresh() {
        getSensors()
        getOccupancy()
    }

    override fun onChangeRoom() {
        getSensors()
    }
}