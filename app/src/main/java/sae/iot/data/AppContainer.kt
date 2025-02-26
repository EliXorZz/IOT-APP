package sae.iot.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import sae.iot.network.PredictionApiService
import sae.iot.network.RoomApiService
import sae.iot.network.SensorApiService

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val RoomsRepository: RoomsRepository
    val SensorsRepository: SensorsRepository
    val PredictionRepository: PredictionRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://10.7.183.213:5001/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val json = Json {
        ignoreUnknownKeys = true // Ignore les champs inconnus dans la réponse JSON
        isLenient = true         // Accepte les données non strictes
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitRoomService: RoomApiService by lazy {
        retrofit.create(RoomApiService::class.java)
    }
    private val retrofitSensorService: SensorApiService by lazy {
        retrofit.create(SensorApiService::class.java)
    }
    private val retrofitPredictionService: PredictionApiService by lazy {
        retrofit.create(PredictionApiService::class.java)
    }

    /**
     * DI implementation for repository
     */
    override val RoomsRepository: RoomsRepository by lazy {
        NetworkRoomsRepository(retrofitRoomService)
    }
    override val SensorsRepository: SensorsRepository by lazy {
        NetworkSensorsRepository(retrofitSensorService)
    }
    override val PredictionRepository: PredictionRepository by lazy {
        NetworkPredictionRepository(retrofitPredictionService)
    }
}
