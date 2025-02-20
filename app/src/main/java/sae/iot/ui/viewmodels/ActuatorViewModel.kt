package sae.iot.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import sae.iot.IotApplication
import sae.iot.data.RoomsRepository

class ActuatorViewModel(
    private val homeViewModel: HomeViewModel,
    private val roomsRepository: RoomsRepository
) : RoomViewModel(homeViewModel, roomsRepository) {

    override fun onChangeRoom() {
    }
}