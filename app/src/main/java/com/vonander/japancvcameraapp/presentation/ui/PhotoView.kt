package com.vonander.japancvcameraapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme

@Composable
fun PhotoView(
    viewModel: MainViewModel,
    onNavigationToCameraPreviewScreen: (String) -> Unit,
) {

    JapanCVCameraAppTheme() {

        Surface(
            color = MaterialTheme.colors.surface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            ) {

                Image(
                    painter = rememberGlidePainter(
                        request =
                        if (viewModel.photoUriString.value.isBlank()) {
                            R.drawable.placeholder_image
                        } else {
                            viewModel.photoUriString.value
                        },
                        previewPlaceholder = R.drawable.placeholder_image,
                    ),
                    contentDescription = "photo taken by device camera",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredSize(300.dp)
                        .padding(6.dp)
                )

                Button(
                    onClick = {
                        val route = Screen.CameraPreview.route
                        onNavigationToCameraPreviewScreen(route)
                    },
                    modifier = Modifier
                        .padding(top = 50.dp)
                ) {
                    Text(text = "Take Photo")
                }
            }
        }


    }
}