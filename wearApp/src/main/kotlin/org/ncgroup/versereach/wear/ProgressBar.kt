package org.ncgroup.versereach.wear

import KottieAnimation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import animateKottieCompositionAsState
import rememberKottieComposition


@Composable
fun ProgressBar(
    isLoading: Boolean
){

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/7b379fe0-2c31-404c-9e93-846722c379c0/wSzhAY0MsQ.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = KottieConstants.IterateForever
    )

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            KottieAnimation(
                composition = composition,
                progress = { animationState.progress },
                modifier = Modifier
                    .size(140.dp)
            )

        }

    }
}

