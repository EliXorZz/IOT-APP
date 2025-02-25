package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.model.DataSensor
import sae.iot.data.SensorsRepository
import sae.iot.model.Sensor
import java.io.IOException

sealed interface AllSensorUiState {
    data class Success(val sensors: Map<String, Sensor>) : AllSensorUiState
    object Error : AllSensorUiState
    object Loading : AllSensorUiState
}

sealed interface DataSensorUiState {
    data class Success(val sensor: DataSensor) : DataSensorUiState
    object Error : DataSensorUiState
    object Loading : DataSensorUiState
}

class SensorsViewModel(
    private val homeViewModel: HomeViewModel,
    private val sensorRepository: SensorsRepository,
) : ViewModel() {

    private val _sensorSelectedUiState: MutableStateFlow<String?> = MutableStateFlow(null)

    val sensorSelectedUiState = _sensorSelectedUiState.asStateFlow()

    var allSensorUiState: AllSensorUiState by mutableStateOf(AllSensorUiState.Loading)
        private set

    var dataSensorUiState: DataSensorUiState by mutableStateOf(DataSensorUiState.Loading)
        private set

    fun fresh() {
        getAllSensor()
    }

    fun changeSensor(sensor: String?) {
        _sensorSelectedUiState.update {
            sensor
        }
        getDataSensor()
    }

    fun getAllSensor() {
        viewModelScope.launch {
            allSensorUiState = AllSensorUiState.Loading
            allSensorUiState = try {
                val site = homeViewModel.currentSiteUiState.value
                val sensorNames = sensorRepository.getSensorsName(site!!.slug())
                sensorNames.keys.firstOrNull()?.let { firstSensor ->
                    changeSensor(firstSensor)
                }
                AllSensorUiState.Success(
                    sensors = sensorNames
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                AllSensorUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                AllSensorUiState.Error
            }
        }
    }

    fun getDataSensor() {
        Log.v("here1", "here1")
        viewModelScope.launch {
            Log.v("here2", "here2")
            dataSensorUiState = DataSensorUiState.Loading
            dataSensorUiState = try {
                val site = homeViewModel.currentSiteUiState.value
                val sensor = sensorRepository.getDataSensor(
                    location = site!!.slug(),
                    sensorId =  sensorSelectedUiState.value!!
                )
                DataSensorUiState.Success(
                    sensor = sensor
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                DataSensorUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                DataSensorUiState.Error
            }
        }
    }
}