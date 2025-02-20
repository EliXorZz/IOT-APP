package sae.iot.network

import kotlinx.serialization.json.Json
import sae.iot.model.Room
import retrofit2.http.GET
import retrofit2.http.Path


interface RoomApiService {

    @GET("api/rooms")
    suspend fun getRooms(): List<Room>

    @GET("api/room/{room}/occupancy")
    suspend fun getOccupancy(@Path("room") room: String): Boolean

}