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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme

@Composable
fun PhotoView(
    viewModel: MainViewModel,
    photoUri: String,
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
                        if (photoUri.isNullOrBlank()) {
                            R.drawable.placeholder_image
                        } else {
                            photoUri
                        },
                        previewPlaceholder = R.drawable.placeholder_image,
                    ),
                    contentDescription = "photo taken by device camera",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .requiredSize(width = 300.dp, height = 400.dp)
                        .padding(6.dp)
                        .scale(scaleX = -1f, scaleY = 1f)
                )

                if(!photoUri.isNullOrBlank()) {

                    Button(
                        onClick = {
                            viewModel.onTriggerEvent(
                                event = PhotoEvent.UploadPhoto(
                                    uriString = photoUri,
                                    completion = {
                                        println("upload_id: ${it.data?.result?.getValue("upload_id")}")
                                    }
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(top = 40.dp)
                    ) {
                        Text(text = "Upload photo")
                    }
                }

                Button(
                    onClick = {
                        val route = Screen.CameraPreview.route
                        onNavigationToCameraPreviewScreen(route)
                    },
                    modifier = Modifier
                        .padding(top = 40.dp)
                ) {
                    Text(text = "Take Photo")
                }
            }
        }
    }
}