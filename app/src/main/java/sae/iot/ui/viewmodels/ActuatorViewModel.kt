package sae.iot.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import sae.iot.IotApplication
import sae.iot.data.RoomsRepository

class ActuatorViewModel(
    private val roomsRepository: RoomsRepository
) : RoomViewModel(roomsRepository) {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val roomsRepository = application.container.RoomsRepository
                ActuatorViewModel(
                    roomsRepository = roomsRepository
                )
            }
        }
    }

    override fun onChangeRoom() {

    }
}