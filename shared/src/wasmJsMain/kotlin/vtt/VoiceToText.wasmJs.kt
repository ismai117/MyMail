package org.ncgroup.versereach.vtt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
internal actual fun VoiceToText(
    text: (String) -> Unit
) {

    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = Icons.Outlined.Mic,
            contentDescription = "body"
        )
    }
}