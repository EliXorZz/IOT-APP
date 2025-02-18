package sae.iot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataSensor(
    @SerialName(value = "discomfort")
    val discomfort: Discomfort,
    @SerialName(value = "measurement")
    val measurement: String,
    @SerialName(value = "x")
    val x: List<Double>,
    @SerialName(value = "y")
    val y: List<Double>
) {
    init {
        require(x.size == y.size) {
            "X and Y lists must have the same size"
        }
    }
}

@Serializable
data class Discomfort(
    @SerialName(value = "causes")
    val causes: String?,
    @SerialName(value = "status")
    val status: Boolean
)


@Serializable
data class OccupancyResponse(
    @SerialName("isOccuped")
    val isOccuped: Boolean
)
