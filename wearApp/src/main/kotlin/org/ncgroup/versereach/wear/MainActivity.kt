package org.ncgroup.versereach.wear

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class VerseReachApplication : Application() {
    companion object {
        lateinit var INSTANCE: VerseReachApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}