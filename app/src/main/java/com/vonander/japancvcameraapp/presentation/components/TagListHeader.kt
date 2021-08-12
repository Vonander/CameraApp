package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun TagListHeader(
    confidence: String,
    tag: String
) {
    val confidenceFloat = confidence.toFloat()
    val confidenceRounded = confidenceFloat.roundToInt()

    Card(
        shape = MaterialTheme.shapes.small,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(bottom = 10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$confidenceRounded% sure that this is some kind of $tag",
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 20.dp,
                        end = 10.dp,
                        bottom = 20.dp
                    )
            )
        }
    }
}