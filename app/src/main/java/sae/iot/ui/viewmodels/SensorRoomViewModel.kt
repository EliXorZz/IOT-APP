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
    private val homeViewModel: HomeViewModel,
    private val sensorRepository: SensorsRepository,
    private val roomsRepository: RoomsRepository
) : RoomViewModel(homeViewModel, roomsRepository) {

    var sensorsUiState: SensorUiState by mutableStateOf(SensorUiState.Loading)
        private set

    private val currentSite = homeViewModel.currentSiteUiState

    fun getSensors() {
        viewModelScope.launch {
            sensorsUiState = SensorUiState.Loading
            sensorsUiState = try {
                val site = homeViewModel.currentSiteUiState.value
                val sensors = sensorRepository.getDataSensorsByRoom(
                    location = site!!.slug(),
                    room = super.roomSelectedUiState.value!!
                )
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

    fun refresh() {
        getSensors()
        getOccupancy()
    }

    fun restarti() {
        getRooms()
    }

    override fun onChangeRoom() {
        getSensors()
    }
}