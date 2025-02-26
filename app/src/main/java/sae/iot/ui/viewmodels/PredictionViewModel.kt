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
import sae.iot.data.RoomsRepository
import sae.iot.model.Prediction
import java.io.IOException

sealed interface PredictionUiState {
    data class Success(val prediction: Prediction) : PredictionUiState
    object Error : PredictionUiState
    object Loading : PredictionUiState
}

class PredictionViewModel(
    private val homeViewModel: HomeViewModel,
    private val roomsRepository: RoomsRepository,
    private val predictionRepository: PredictionRepository
) : RoomViewModel(homeViewModel, roomsRepository) {

    var predictionUiState: PredictionUiState by mutableStateOf(PredictionUiState.Loading)
        private set

    init {
        getPrediction()
    }

    fun getPrediction() {
        viewModelScope.launch {
            predictionUiState = PredictionUiState.Loading
            predictionUiState = try {
                val site = homeViewModel.currentSiteUiState.value
                PredictionUiState.Success(
                    prediction = predictionRepository.getPredictionByRoom(
                        location = site!!.slug(),
                        room = super.roomSelectedUiState.value!!
                    )
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

    override fun onChangeRoom() {
        getPrediction()
    }
}