package com.vonander.japancvcameraapp.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavControllerNavigate: () -> Unit
) {

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween (
            durationMillis = 1000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)

        onNavControllerNavigate()
    }

    JapanCVCameraAppTheme {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
        ) {

            Image(
                painter = rememberGlidePainter(
                    request = R.drawable.paypaylogo,
                    previewPlaceholder = R.drawable.placeholder_image,
                ),
                contentDescription = "paypay logo",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .padding(bottom = 350.dp)
                    .alpha(alpha = alphaAnim.value)
            )

            CircularIndeterminateProgressBar(
                isDisplayed = true,
                color = MaterialTheme.colors.onPrimary,
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
            )
        }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    SplashScreen {}
}

@Composable
@Preview
fun SplashScreenDarkPreview(uiMode: Int = UI_MODE_NIGHT_YES) {
    SplashScreen {}
}
