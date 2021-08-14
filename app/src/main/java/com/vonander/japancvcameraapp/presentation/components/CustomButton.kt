package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(
    modifier: Modifier,
    buttonText: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.body2
        )
    }

}