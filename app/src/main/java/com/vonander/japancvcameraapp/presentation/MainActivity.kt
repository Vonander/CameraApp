package com.vonander.japancvcameraapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.presentation.ui.PhotoView
import com.vonander.japancvcameraapp.presentation.ui.MainViewModel
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import com.vonander.japancvcameraapp.util.REQUIRED_PERMISSIONS
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkCameraPermissions()
    }

    private fun checkCameraPermissions() {
        if(hasPermissions(REQUIRED_PERMISSIONS)) {
            setViewContent()
        }else {
            permissionRequest.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun setViewContent() {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.screenOrientation.value = resources.configuration.orientation

        setContent {
            JapanCVCameraAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    //CameraPreview(viewModel)
                    PhotoView(viewModel)
                }
            }
        }
    }

    private fun hasPermissions(
        permissions: Array<String>
    ): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

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