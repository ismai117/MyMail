package org.ncgroup.mymail.sharedComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import org.ncgroup.mymail.navigation.BottomNavigation


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navigator: Navigator
){
    BottomNavigation(
        navigator = navigator
    )
}