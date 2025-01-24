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

sealed interface ActuatorRoomUiState {
    data class Success(val rooms: List<Room>) : ActuatorRoomUiState
    object Error : ActuatorRoomUiState
    object Loading : ActuatorRoomUiState
}

class ActuatorViewModel(
    private val roomsRepository: RoomsRepository
) : ViewModel() {
    var actuatorRoomUiState: ActuatorRoomUiState by mutableStateOf(ActuatorRoomUiState.Loading)
        private set

    init {
        getRooms()
    }

    fun getRooms() {
        viewModelScope.launch {
            actuatorRoomUiState = ActuatorRoomUiState.Loading
            actuatorRoomUiState = try {
                val rooms = roomsRepository.getRoomsNames()
                ActuatorRoomUiState.Success(
                    rooms = rooms
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                ActuatorRoomUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                ActuatorRoomUiState.Error
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