package sae.iot.data

import sae.iot.model.Room
import sae.iot.network.RoomApiService

interface RoomsRepository {
    suspend fun getRoomsNames(location: String): List<Room>
    suspend fun getOccupancy(room: String, location: String): Boolean
}

class NetworkRoomsRepository(
    private val RoomApiService: RoomApiService
) : RoomsRepository {
    override suspend fun getRoomsNames(location: String): List<Room> = RoomApiService.getRooms(location)
    override suspend fun getOccupancy(room: String, location: String): Boolean = RoomApiService.getOccupancy(room, location)
}
