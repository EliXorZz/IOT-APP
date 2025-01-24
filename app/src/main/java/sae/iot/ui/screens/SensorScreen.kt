package sae.iot.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import sae.iot.ui.components.HomeNavigation

@Composable
fun SensorScreen(
    navController: NavHostController,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column {
        HomeNavigation(navController, index)
    }
}