package sae.iot.ui.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import sae.iot.R
import sae.iot.ui.viewmodels.Site
import sae.iot.ui.viewmodels.HomeViewModel

@Composable
fun TopBar(
    alert: String? = null,
    homeViewModel: HomeViewModel,
    navigationController: NavHostController,
    site: Site,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 20.dp)
        ) {
            Icon(
                Icons.Rounded.ArrowBackIosNew,
                contentDescription = "back",

                modifier = Modifier.clickable {
                    homeViewModel.setCurrentSite(navigationController, null)
                }
            )

            Row {
                Image(
                    painter = painterResource(site.logo()),
                    contentDescription = "logo",

                    modifier = Modifier.size(55.dp)
                )

                Column(
                    modifier = Modifier
                        .offset(x = 10.dp)
                ) {
                    Text(LocalContext.current.getString(R.string.welcome))
                    Text(
                        text = site.realName(),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.W700
                    )
                }
            }
        }

        alert?.let {
            TopAlert(it)
        }
    }
}