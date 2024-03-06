package org.ncgroup.versereach

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.ncgroup.versereach.email.EmailScreen
import org.ncgroup.versereach.sms.SmsScreen


object MainScreen : Screen {
    @Composable
    override fun Content() {
        MainScreenContent()
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier
){
    TabNavigator(EmailScreen){ tab ->
        Scaffold(
            bottomBar = {
                NavigationBar {
                    TabNavigationItem(EmailScreen)
                    TabNavigationItem(SmsScreen)
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ){
                    CurrentTab()
                }
            }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
        label = {
            Text(
                text = tab.options.title
            )
        }
    )
}