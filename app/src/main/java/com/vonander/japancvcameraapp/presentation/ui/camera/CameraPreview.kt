package com.vonander.japancvcameraapp.presentation.ui.camera

import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.vonander.japancvcameraapp.interactors.StartCamera
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.CameraPreviewToolbar
import com.vonander.japancvcameraapp.presentation.components.DecoupledSnackbar
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.presentation.ui.overlay.FaceDetectionOverlay
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoViewViewModel
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun CameraPreview(
    cameraPreviewViewModel: CameraPreviewViewModel,
    photoViewViewModel: PhotoViewViewModel,
    onNavControllerNavigate: (String) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val snackbarHostState = remember{ SnackbarHostState() }

    val imageCapture = remember {
        ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()
    }

    JapanCVCameraAppTheme {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            val faceDetectionOverlay = FaceDetectionOverlay(context)
            faceDetectionOverlay.setOrientation(1)

            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)

                    val startCamera = StartCamera(
                        context = context,
                        previewView = previewView,
                        lifecycleOwner = lifecycleOwner,
                        imageCapture = imageCapture
                    )

                    startCamera.execute().onEach { dataState ->

                        dataState.error?.let { message ->
                            showSnackbar(
                                snackbarHostState = snackbarHostState,
                                message = message,
                            )
                        }

                    }.launchIn(cameraPreviewViewModel.viewModelScope)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            AndroidView(
                factory = { faceDetectionOverlay },
                modifier = Modifier.fillMaxSize()
            )

            CameraPreviewToolbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                onNavigationToPhotoViewScreen = onNavControllerNavigate,
                onTakePhoto = {
                    cameraPreviewViewModel.onTriggerEvent(
                        event = PhotoEvent.TakePhoto(
                            imageCapture = imageCapture,
                            completion = { dataState ->

                                dataState.data?.let { photoUri ->
                                    photoViewViewModel.photoUri.value = photoUri
                                    onNavControllerNavigate(Screen.PhotoView.route)
                                }

                                dataState.error?.let { message ->
                                    showSnackbar(
                                        snackbarHostState = snackbarHostState,
                                        message = message,
                                    )
                                }
                            }
                        )
                    )
                }
            )

            DecoupledSnackbar(
                snackbarHostState = snackbarHostState,
                onclick = {snackbarHostState.currentSnackbarData?.dismiss()}
            )
        }
    }
}

private fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    CoroutineScope(Dispatchers.Main).launch {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = "Hide",
            duration = SnackbarDuration.Short
        )
    }
}