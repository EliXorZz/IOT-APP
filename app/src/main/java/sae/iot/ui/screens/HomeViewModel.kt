package sae.iot.ui.screens

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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.IotApplication
import sae.iot.data.RoomsRepository
import sae.iot.model.DataSensor
import sae.iot.data.SensorsRepository
import sae.iot.model.Room
import java.io.IOException

sealed interface RoomUiState {
    data class Success(val rooms: List<Room>) : RoomUiState
    object Error : RoomUiState
    object Loading : RoomUiState
}

sealed interface SensorUiState {
    data class Success(val sensors: Map<String, DataSensor>) : SensorUiState
    object Error : SensorUiState
    object Loading : SensorUiState
}

class HomeViewModel(
    private val sensorRepository: SensorsRepository,
    private val roomsRepository: RoomsRepository
) : ViewModel() {
    var roomUiState: RoomUiState by mutableStateOf(RoomUiState.Loading)
        private set
    var sensorsUiState: SensorUiState by mutableStateOf(SensorUiState.Loading)
        private set

    init {
        getRooms()
    }

    fun getRooms() {
        viewModelScope.launch {
            roomUiState = RoomUiState.Loading
            roomUiState = try {
                val rooms = roomsRepository.getRoomsNames()
                RoomUiState.Success(
                    rooms = rooms
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                RoomUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                RoomUiState.Error
            }
        }
    }


    fun getSensors(room: String) {
        viewModelScope.launch {
            sensorsUiState = SensorUiState.Loading
            sensorsUiState = try {
                val sensors = sensorRepository.getDataSensorsByRoom(room)
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
                HomeViewModel(
                    sensorRepository = sensorsRepository,
                    roomsRepository = roomsRepository
                )
            }
        }
    }
}