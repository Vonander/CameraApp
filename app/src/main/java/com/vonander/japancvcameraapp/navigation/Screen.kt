package com.vonander.japancvcameraapp.navigation

sealed class Screen(
    val route: String
) {
    object PhotoView: Screen("photoView")
    object CameraPreview: Screen("cameraPreview")
    object AboutView: Screen("aboutView")
    object SplashScreen: Screen("splashScreen")
}
