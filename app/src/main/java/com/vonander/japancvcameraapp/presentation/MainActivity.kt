package com.vonander.japancvcameraapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.AboutView
import com.vonander.japancvcameraapp.presentation.components.SplashScreen
import com.vonander.japancvcameraapp.presentation.ui.camera.CameraPreview
import com.vonander.japancvcameraapp.presentation.ui.camera.CameraPreviewViewModel
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoView
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoViewViewModel
import com.vonander.japancvcameraapp.util.REQUIRED_PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val context = this

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    private fun checkPermissions() {
        if(hasPermissions(REQUIRED_PERMISSIONS)) {
            setViewContent()
        }else {
            permissionRequest.launch(REQUIRED_PERMISSIONS)
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    private fun setViewContent() {
        setContent {

            val navController = rememberAnimatedNavController()
            var photoViewViewModel: PhotoViewViewModel = hiltViewModel()

            AnimatedNavHost(
                navController = navController,
                startDestination = Screen.SplashScreen.route
            ) {

                composable(
                    route = Screen.SplashScreen.route
                ) {
                    SplashScreen(
                        onNavControllerNavigate = {
                        navController.navigate(Screen.PhotoView.route)
                    })
                }

                composable(
                    route = Screen.PhotoView.route
                ) {
                    photoViewViewModel = hiltViewModel<PhotoViewViewModel>()
                    PhotoView(
                        viewModel = photoViewViewModel,
                        onNavControllerNavigate = {
                            navController.navigate(it)
                        }
                    )
                }

                composable(
                    route = Screen.CameraPreview.route,
                    enterTransition = {_,_ ->
                        slideInVertically(initialOffsetY = {200},
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(1000))
                    },
                    exitTransition = {_,_ ->
                        slideOutVertically(
                            targetOffsetY = {200},
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(animationSpec = tween(1000))
                    }
                ) {
                    val cameraPreviewViewModel = hiltViewModel<CameraPreviewViewModel>()
                    CameraPreview(
                        cameraPreviewViewModel = cameraPreviewViewModel,
                        photoViewViewModel = photoViewViewModel,
                        onNavControllerNavigate = {
                            navController.navigate(it)
                        }
                    )
                }

                composable(
                    route = Screen.AboutView.route,
                    enterTransition = {_,_ ->
                        fadeIn(animationSpec = tween(1000))
                    },
                    exitTransition = {_,_ ->
                       fadeOut(animationSpec = tween(1000))
                    }
                ) {
                    AboutView(
                        context = context
                    )
                }
            }
        }
    }

    private fun hasPermissions(
        permissions: Array<String>
    ): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                setViewContent()
            } else {
                showToast("Permissions are needed for this app to function properly")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {}