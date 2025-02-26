package sae.iot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prediction(
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