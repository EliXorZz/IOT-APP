package sae.iot.ui.viewmodels

import sae.iot.data.RoomsRepository

class ActuatorViewModel(
    private val homeViewModel: HomeViewModel,
    private val roomsRepository: RoomsRepository
) : RoomViewModel(homeViewModel, roomsRepository) {


    override fun onChangeRoom() {
    }
}