package com.vonander.japancvcameraapp.presentation.components

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme

@Composable
fun CustomBottomBar(
    onNavControllerNavigate: (String) -> Unit
) {

    val iconSize = 35.dp

    JapanCVCameraAppTheme {

        BottomNavigation(
            elevation = 12.dp,
            backgroundColor = MaterialTheme.colors.onBackground
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = "home icon",
                        tint = Color.White,
                        modifier = Modifier.requiredSize(iconSize)
                    )
                },
                selected = false,
                onClick = {
                    println("okej click! home")
                    onNavControllerNavigate(Screen.PhotoView.route)
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Outlined.Camera,
                        contentDescription = "camera icon",
                        tint = Color.White,
                        modifier = Modifier.requiredSize(iconSize)
                    )
                },
                selected = false,
                onClick = {
                    println("okej click! camera")
                    onNavControllerNavigate(Screen.CameraPreview.route)
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "info icon",
                        tint = Color.White,
                        modifier = Modifier.requiredSize(iconSize)
                    )
                },
                selected = false,
                onClick = {
                    println("okej click! info")
                    //onNavControllerNavigate(Screen.CameraPreview.route)
                }
            )
        }
    }
}