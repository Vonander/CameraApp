package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vonander.japancvcameraapp.navigation.Screen

@Composable
fun CameraPreviewToolbar(
    modifier: Modifier,
    onNavigationToPhotoViewScreen: (String) -> Unit,
    onTakePhoto: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0f)
    ) {

        Row {
            IconButton(
                modifier = Modifier.padding(start = 10.dp, top = 30.dp),
                onClick = {
                    onNavigationToPhotoViewScreen(Screen.PhotoView.route)
                }
            ) {
                Icon(Icons.Outlined.ArrowBack,
                    contentDescription = "arrow back icon",
                    tint = Color.White,
                    modifier = Modifier.requiredSize(30.dp)
                )
            }
        }

        Column {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .requiredSize(100.dp),
                onClick = {
                    onTakePhoto()
                }
            ) {
                Icon(Icons.Outlined.Circle,
                    contentDescription = "circle icon",
                    tint = Color.White,
                    modifier = Modifier.requiredSize(40.dp)
                )
            }
        }
    }
}