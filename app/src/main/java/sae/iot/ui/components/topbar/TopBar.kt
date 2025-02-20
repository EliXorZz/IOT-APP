package sae.iot.ui.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sae.iot.R
import sae.iot.ui.viewmodels.Build

@Composable
fun TopBar(
    alert: String? = null,
    build: Build,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .statusBarsPadding()
                .padding(vertical = 20.dp)
        ) {
            Image(
                painter = painterResource(build.logo()),
                contentDescription = "logo",

                modifier = Modifier.size(55.dp)
            )

            Column(
                modifier = Modifier
                    .offset(x = 10.dp)
            ) {
                Text(LocalContext.current.getString(R.string.welcome))
                Text(
                    text = build.realName(),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }

        alert?.let {
            TopAlert(it)
        }
    }
}