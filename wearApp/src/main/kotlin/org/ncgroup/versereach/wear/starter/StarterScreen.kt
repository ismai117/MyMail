package org.ncgroup.versereach.wear.starter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text


@Composable
fun StarterScreen(
    modifier: Modifier = Modifier,
    navigateToEmailScreen: () -> Unit,
    navigateToSMSScreen: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navigateToEmailScreen()
                }
            ){
                Text(
                    text = "Email"
                )
            }
            Button(
                onClick = {
                    navigateToSMSScreen()
                }
            ){
                Text(
                    text = "SMS"
                )
            }
        }
    }
}