package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    private val homeViewModel: HomeViewModel,
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _roomSelectedUiState: MutableStateFlow<String?> = MutableStateFlow(null)
    val roomSelectedUiState = _roomSelectedUiState.asStateFlow()

    private val _alertOccupiedUiState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val alertOccupiedUiState = _alertOccupiedUiState.asStateFlow()

    var roomUiState: RoomUiState by mutableStateOf(RoomUiState.Loading)
        private set

    var occupancyUiState: OccupancyUiState by mutableStateOf(OccupancyUiState.Loading)
        private set

    fun changeRoom(room: String?) {
        _roomSelectedUiState.update {
            room
        }
        onChangeRoom()
        getOccupancy()
    }

    fun setAlertClose(){
        _alertOccupiedUiState.update {
            false
        }
    }

    fun getRooms() {
        viewModelScope.launch {
            roomUiState = RoomUiState.Loading
            roomUiState = try {
                val site = homeViewModel.currentSiteUiState.value
                val rooms = roomsRepository.getRoomsNames(site!!.slug())
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
            try {
                val site = homeViewModel.currentSiteUiState.value
                val isOccupied = roomsRepository.getOccupancy(
                    location = site!!.slug(),
                    room = roomSelectedUiState.value!!
                )
                occupancyUiState = OccupancyUiState.Success(
                    occupied = isOccupied
                )
                _alertOccupiedUiState.update {
                    isOccupied
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                occupancyUiState = OccupancyUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                occupancyUiState = OccupancyUiState.Error
            }
        }
    }

    abstract fun onChangeRoom()
}