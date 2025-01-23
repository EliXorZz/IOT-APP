package sae.iot.data

import sae.iot.model.Classroom
import sae.iot.network.RoomApiService

/**
 * Repository that fetch mars photos list from marsApi.
 */
interface ClassroomsRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getClassroomsNames(): List<Classroom>
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkClassroomsRepository(
    private val ClassroomApiService: RoomApiService
) : ClassroomsRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getClassroomsNames(): List<Classroom> = ClassroomApiService.getClassrooms()
}
