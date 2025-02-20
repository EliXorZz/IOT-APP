package sae.iot.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import sae.iot.IotApplication
import sae.iot.R
import sae.iot.ui.IOTScreen

enum class Site {
    TETRAS,
    IUT;

    fun realName(): String {
        return when (this) {
            TETRAS -> "Tetras"
            IUT -> "IUT Annecy"
        }
    }

    fun slug(): String {
        return when (this) {
            TETRAS -> "tetras"
            IUT -> "iut"
        }
    }

    fun logo(): Int {
        return when (this) {
            TETRAS -> R.drawable.tetras_logo
            IUT -> R.drawable.iut_logo
        }
    }
}

enum class ViewType {
    CURRENT,
    CHART
}

class HomeViewModel : ViewModel() {
    private val _currentSiteUiState = MutableStateFlow<Site?>(null)
    val currentSiteUiState: StateFlow<Site?> = _currentSiteUiState.asStateFlow()


    private val _selectedIndexUiState = MutableStateFlow(0)
    val selectedIndexUiState = _selectedIndexUiState.asStateFlow()

    private val _viewTypeUiState = MutableStateFlow<ViewType>(ViewType.CURRENT)
    val viewTypeUiState = _viewTypeUiState.asStateFlow()


    fun setCurrentSite(navigationController: NavHostController, site: Site?) {
        if (site != null) {

            navigationController.navigate(IOTScreen.Room.name)
        } else {
            navigationController.navigate(IOTScreen.Main.name)
        }

        _currentSiteUiState.value = site
    }

    fun setSelectedIndex(navigationController: NavHostController, index: Int) {
        when (index) {
            0 -> navigationController.navigate(IOTScreen.Room.name)
            1 -> navigationController.navigate(IOTScreen.Sensor.name)
        }

        _selectedIndexUiState.update {
            index
        }
    }

    fun switchViewType() {
        if (_viewTypeUiState.value == ViewType.CURRENT) {
            _viewTypeUiState.update {
                ViewType.CHART
            }
        } else if (_viewTypeUiState.value == ViewType.CHART) {
            _viewTypeUiState.update {
                ViewType.CURRENT
            }
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