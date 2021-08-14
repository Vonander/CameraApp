package com.vonander.japancvcameraapp.presentation.ui.camera

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.CameraPreviewToolbar
import com.vonander.japancvcameraapp.presentation.components.DecoupledSnackbar
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.presentation.ui.overlay.FaceDetectionOverlay
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoViewViewModel
import com.vonander.japancvcameraapp.presentation.utils.FaceAnalyzer
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import com.vonander.japancvcameraapp.util.TAG
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
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val imageCapture = remember {
        ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()
    }

    JapanCVCameraAppTheme() {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            val faceDetectionOverlay = FaceDetectionOverlay(context)
            faceDetectionOverlay.setOrientation(1)

            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val cameraLens =
                            if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                                CameraSelector.LENS_FACING_FRONT
                            } else {
                                CameraSelector.LENS_FACING_BACK
                            }

                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(cameraLens)
                            .build()

                        val analysisUseCase = ImageAnalysis.Builder()
                            .build()
                            .also {
                                it.setAnalyzer(
                                    executor, FaceAnalyzer(
                                        lifecycle = lifecycleOwner.lifecycle,
                                        overlay = faceDetectionOverlay
                                    )
                                )
                            }

                        try {
                            cameraProvider.unbindAll()

                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture,
                                analysisUseCase
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "CameraPreview Use case binding failed", e)
                        }

                    }, executor)

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
                                        viewModel = cameraPreviewViewModel,
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
    viewModel: CameraPreviewViewModel,
    snackbarHostState: SnackbarHostState,
    message: String
) {
    viewModel.viewModelScope.launch {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = "Hide",
            duration = SnackbarDuration.Short
        )
    }
}