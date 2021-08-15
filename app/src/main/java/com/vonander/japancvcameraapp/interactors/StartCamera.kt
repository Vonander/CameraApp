package com.vonander.japancvcameraapp.interactors

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.presentation.ui.camera.CameraState
import com.vonander.japancvcameraapp.presentation.ui.overlay.FaceDetectionOverlay
import com.vonander.japancvcameraapp.presentation.utils.FaceAnalyzer
import com.vonander.japancvcameraapp.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class StartCamera(
    private val context: Context,
    private val previewView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val imageCapture: ImageCapture,
    private val faceDetectionOverlay: FaceDetectionOverlay
) {
    fun execute(): Flow<DataState<Any>> = flow {
        try {

            emit(DataState.loading())

            val executor = ContextCompat.getMainExecutor(context)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            var cameraLensFacing = CameraState.Front.facing

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                if (!deviceHasAnyCamera(context = context)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        emit(DataState.error<String>("Device has no camera"))
                    }
                }

                if (deviceHasFrontCamera(context = context)) {
                    cameraLensFacing = CameraState.Front.facing
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraLensFacing)
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

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    analysisUseCase
                )

            }, executor)

            emit(DataState.success<String>("Start Camera success!"))

        } catch (e: Exception) {
            Log.e(TAG, "Start camera error $e", e.cause)
            emit(DataState.error<String>("CameraPreview Use case binding failed $e"))
        }
    }
}

private fun deviceHasFrontCamera(
    context: Context
): Boolean {
    return context.packageManager.hasSystemFeature(
        PackageManager.FEATURE_CAMERA_FRONT
    )
}

private fun deviceHasAnyCamera(
    context: Context
): Boolean {
    return context.packageManager.hasSystemFeature(
        PackageManager.FEATURE_CAMERA_ANY
    )
}