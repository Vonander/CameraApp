package com.vonander.japancvcameraapp

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
import com.vonander.japancvcameraapp.presentation.components.CameraPreview
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import com.vonander.japancvcameraapp.util.REQUIRED_PERMISSIONS

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
        setContent {
            JapanCVCameraAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CameraPreview()
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {}