package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean,
    color: Color,
    modifier: Modifier
) {
    if (isDisplayed) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = color
            )
        }
    }
}