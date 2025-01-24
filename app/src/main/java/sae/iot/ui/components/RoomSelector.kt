package sae.iot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sae.iot.model.Room

@Composable
fun RoomSelector(
    rooms: List<Room>,
    selected: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            items(rooms) { room ->
                val isSelected = selected == room.name

                Text(
                    text = room.name,
                    fontSize = if (isSelected) 23.sp else 18.sp,
                    fontWeight = if (isSelected) FontWeight.W700 else FontWeight.W400
                )
            }
        }
    }
}