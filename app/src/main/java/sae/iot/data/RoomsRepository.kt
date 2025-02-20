package sae.iot.data

import sae.iot.model.Room
import sae.iot.network.RoomApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface RoomsRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getRoomsNames(location: String): List<Room>
    suspend fun getOccupancy(location: String, room: String): Boolean
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkRoomsRepository(
    private val RoomApiService: RoomApiService
) : RoomsRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getRoomsNames(location: String): List<Room> = RoomApiService.getRooms(location)
    override suspend fun getOccupancy(location: String, room: String): Boolean = RoomApiService.getOccupancy(location, room)
}
