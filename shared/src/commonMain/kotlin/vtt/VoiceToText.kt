package org.ncgroup.versereach.vtt


import androidx.compose.runtime.Composable


@Composable
internal expect fun VoiceToText(
    text: (String) -> Unit
)