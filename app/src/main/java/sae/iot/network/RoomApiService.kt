package sae.iot.network

import sae.iot.model.Room
import retrofit2.http.GET


interface RoomApiService {

    @GET("api/rooms")
    suspend fun getRooms(): List<Room>
}