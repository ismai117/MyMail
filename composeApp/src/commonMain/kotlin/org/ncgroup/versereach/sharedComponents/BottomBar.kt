package org.ncgroup.versereach.sharedComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import org.ncgroup.versereach.navigation.BottomNavigation


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navigator: Navigator
){
    BottomNavigation(
        navigator = navigator
    )
}