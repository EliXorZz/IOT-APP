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

val DEFAULT_ROOM = null

sealed interface RoomUiState {
    data class Success(val rooms: List<Room>) : RoomUiState
    object Error : RoomUiState
    object Loading : RoomUiState
}

abstract class RoomViewModel(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _roomSelectedUiState: MutableStateFlow<String?> = MutableStateFlow(DEFAULT_ROOM)

    val roomSelectedUiState = _roomSelectedUiState.asStateFlow()

    var roomUiState: RoomUiState by mutableStateOf(RoomUiState.Loading)
        private set


    init {
        getRooms()
    }

    fun changeRoom(room: String?) {
        _roomSelectedUiState.update {
            room
        }
        onChangeRoom()
    }

    abstract fun onChangeRoom()

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
}