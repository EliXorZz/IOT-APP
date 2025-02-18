package sae.iot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import sae.iot.ui.components.HomeNavigation
import sae.iot.ui.viewmodels.HomeViewModel

@Composable
fun SensorScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val subMenuIndex by homeViewModel.selectedIndexUiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        HomeNavigation(
            selectedIndex = subMenuIndex,
            onSelectedIndex = {
                homeViewModel.setSelectedIndex(navController, it)
            }
        )
    }
}