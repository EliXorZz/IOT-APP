package sae.iot.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sae.iot.data.PredictionRepository
import sae.iot.model.Prediction
import java.io.IOException

sealed interface PredictionUiState {
    data class Success(val prediction: Prediction) : PredictionUiState
    object Error : PredictionUiState
    object Loading : PredictionUiState
}

abstract class PredictionViewModel(
    private val predictionRepository: PredictionRepository
) : ViewModel() {

    var predictionUiState: PredictionUiState by mutableStateOf(PredictionUiState.Loading)
        private set

    fun getPrediction() {
        viewModelScope.launch {
            predictionUiState = PredictionUiState.Loading
            predictionUiState = try {
                PredictionUiState.Success(
                    prediction = predictionRepository.getPrediction()
                )
            } catch (e: IOException) {
                Log.e("IOException", e.toString(), e)
                PredictionUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString(), e)
                PredictionUiState.Error
            }
        }
    }
}