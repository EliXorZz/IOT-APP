package sae.iot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import sae.iot.ui.viewmodels.Build
import sae.iot.ui.viewmodels.HomeViewModel

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Séléctionner un batiment.",
            fontSize = 25.sp
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,

            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Build.TETRAS.logo()),
                contentDescription = "logo",

                modifier = Modifier
                    .size(140.dp)
                    .clickable {
                        homeViewModel.setCurrentBuild(navController, Build.TETRAS)
                    }
            )

            Image(
                painter = painterResource(Build.IUT.logo()),
                contentDescription = "logo",

                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        homeViewModel.setCurrentBuild(navController, Build.IUT)
                    }
            )
        }
    }
}