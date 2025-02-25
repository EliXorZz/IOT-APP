package sae.iot.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import sae.iot.ui.viewmodels.HomeViewModel
import sae.iot.ui.viewmodels.ViewType

@Composable
fun SwitchViewButton(
    homeViewModel: HomeViewModel,
    type: ViewType,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { homeViewModel.switchViewType() },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (type == ViewType.CURRENT) Icons.Default.BarChart else Icons.Default.Numbers,
            contentDescription = "Switch type",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}