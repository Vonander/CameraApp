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
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = modifier,
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.body2
        )
    }

}