package org.ncgroup.versereach.wear.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(
        navigateToStarterScreen = {}
    )
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToStarterScreen: () -> Unit
){

    LaunchedEffect(Unit){
        delay(2000)
        navigateToStarterScreen()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "VerseReach",
            fontSize = 32.sp
        )

    }
}