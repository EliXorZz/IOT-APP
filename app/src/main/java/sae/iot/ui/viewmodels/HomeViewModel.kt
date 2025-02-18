package sae.iot.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sae.iot.IotApplication
import sae.iot.ui.IOTScreen

class HomeViewModel : ViewModel() {
    private val _selectedIndexUiState = MutableStateFlow(0)
    val selectedIndexUiState = _selectedIndexUiState.asStateFlow()

    fun setSelectedIndex(navigationController: NavHostController, index: Int) {
        when (index) {
            0 -> navigationController.navigate(IOTScreen.Room.name)
            1 -> navigationController.navigate(IOTScreen.Sensor.name)
        }

        _selectedIndexUiState.update {
            index
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as IotApplication)
                val roomsRepository = application.container.RoomsRepository
                HomeViewModel()
            }
        }
    }
}