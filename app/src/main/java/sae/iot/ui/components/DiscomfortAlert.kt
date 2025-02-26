package sae.iot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DiscomfortAlert(
    message: String,
    intensity: Int,
    onDismiss: () -> Unit
) {
    var icon = Icons.Rounded.Warning
    var iconTint = MaterialTheme.colorScheme.error
    var textColor = MaterialTheme.colorScheme.onErrorContainer
    var containerColor = MaterialTheme.colorScheme.errorContainer

    when (intensity) {
        1 -> {
            icon = Icons.Rounded.Info
            iconTint = Color(0xFF0288D1)
            textColor = Color(0xFF01579B)
            containerColor = Color(0xFFE1F5FE)
        }
        2 -> {
            icon = Icons.Rounded.ErrorOutline
            iconTint = Color(0xFFFF9800)
            textColor = Color(0xFFF57C00)
            containerColor = Color(0xFFFFF3E0)
        }
        3 -> {
            icon = Icons.Rounded.Warning
            iconTint = MaterialTheme.colorScheme.error
            textColor = MaterialTheme.colorScheme.onErrorContainer
            containerColor = MaterialTheme.colorScheme.errorContainer
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Warning icon",
                    tint = iconTint
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Dismiss alert",
                    tint = iconTint
                )
            }
        }
    }
}

@Preview
@Composable
fun DiscomfortAlertPreview() {
    DiscomfortAlert(
        message = "Température",
        intensity = 1,
        onDismiss = {}
    )
}
