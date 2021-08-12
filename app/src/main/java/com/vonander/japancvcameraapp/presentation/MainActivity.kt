package com.vonander.japancvcameraapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.datastore.PhotoDataStore
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.ui.PhotoView
import com.vonander.japancvcameraapp.presentation.ui.camera.CameraPreview
import com.vonander.japancvcameraapp.presentation.ui.camera.CameraPreviewViewModel
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoViewViewModel
import com.vonander.japancvcameraapp.util.REQUIRED_PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val context = this
    private var dataStore = PhotoDataStore()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
    }

    @ExperimentalFoundationApi
    private fun checkPermissions() {
        if(hasPermissions(REQUIRED_PERMISSIONS)) {
            setViewContent()
        }else {
            permissionRequest.launch(REQUIRED_PERMISSIONS)
        }
    }

    @ExperimentalFoundationApi
    private fun setViewContent() {
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.PhotoView.route
            ) {

                composable(route = Screen.PhotoView.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: PhotoViewViewModel = viewModel("PhotoViewViewModel", factory)
                    PhotoView(
                        viewModel = viewModel,
                        photoUri = getLatestStoredPhoto(context),
                        onNavControllerNavigate = {
                            navController.navigate(it)
                        }
                    )
                }

                composable(route = Screen.CameraPreview.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: CameraPreviewViewModel = viewModel("CameraPreviewViewModel", factory)
                    CameraPreview(
                        viewModel = viewModel,
                        onNavControllerNavigate = {
                            navController.navigate(it)
                        }
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

    @ExperimentalFoundationApi
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                setViewContent()
            } else {
                showToast("Permissions not granted.")
            }
        }

    private fun getLatestStoredPhoto(context: Context): String = runBlocking {
        return@runBlocking dataStore.getPhotoUriString(context)
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