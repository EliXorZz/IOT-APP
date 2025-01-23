package sae.iot.ui.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAlert(
    title: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val color = Color(0xFFFFB1A9)

    Box(
        modifier = modifier
            .clip(shape)
            .background(color)
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp
        )
    }
}