package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.data.RoomsRepository
import sae.iot.model.Room
import java.io.IOException

sealed interface RoomUiState {
    data class Success(val rooms: List<Room>) : RoomUiState
    object Error : RoomUiState
    object Loading : RoomUiState
}

sealed interface OccupancyUiState {
    data class Success(val occupied: Boolean) : OccupancyUiState
    object Error : OccupancyUiState
    object Loading : OccupancyUiState
}

abstract class RoomViewModel(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _roomSelectedUiState: MutableStateFlow<String?> = MutableStateFlow(null)
    val roomSelectedUiState = _roomSelectedUiState.asStateFlow()

    private val _roomOccupiedUiState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val roomOccupiedUiState = _roomOccupiedUiState.asStateFlow()

    var roomUiState: RoomUiState by mutableStateOf(RoomUiState.Loading)
        private set

    var occupancyUiState: OccupancyUiState by mutableStateOf(OccupancyUiState.Loading)
        private set

    init {
        getRooms()
    }

    fun changeRoom(room: String?) {
        _roomSelectedUiState.update {
            room
        }
        onChangeRoom()
        getOccupancy()
    }

    fun getRooms() {
        viewModelScope.launch {
            roomUiState = RoomUiState.Loading
            roomUiState = try {
                val rooms = roomsRepository.getRoomsNames()
                changeRoom(rooms[0].name)
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

    fun getOccupancy() {
        viewModelScope.launch {
            occupancyUiState = OccupancyUiState.Loading
            occupancyUiState = try {
                val isOccupied = roomsRepository.getOccupancy(roomSelectedUiState.value!!)
                OccupancyUiState.Success(
                    occupied = isOccupied
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                OccupancyUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                OccupancyUiState.Error
            }
        }
    }

    abstract fun onChangeRoom()
}