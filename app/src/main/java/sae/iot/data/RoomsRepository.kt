package sae.iot.data

import sae.iot.model.Room
import sae.iot.network.RoomApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface RoomsRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getRoomsNames(): List<Room>
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkRoomsRepository(
    private val RoomApiService: RoomApiService
) : RoomsRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getRoomsNames(): List<Room> = RoomApiService.getRooms()
}
