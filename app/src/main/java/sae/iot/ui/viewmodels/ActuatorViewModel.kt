package sae.iot.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import sae.iot.IotApplication
import sae.iot.data.RoomsRepository

class ActuatorViewModel(
    private val location: Site,
    private val roomsRepository: RoomsRepository
) : RoomViewModel(location, roomsRepository) {

    companion object {
        private var locationSite: Site = Site.IUT

        fun initialize(location: Site) {
            locationSite = location
        }

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val roomsRepository = application.container.RoomsRepository
                ActuatorViewModel(
                    location = locationSite,
                    roomsRepository = roomsRepository
                )
            }
        }
    }

    override fun onChangeRoom() {

    }
}