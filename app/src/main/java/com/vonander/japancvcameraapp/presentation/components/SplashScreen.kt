package com.vonander.japancvcameraapp.presentation.components

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavControllerNavigate: () -> Unit
) {
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
            )

            CircularIndeterminateProgressBar(
                isDisplayed = true,
                color = MaterialTheme.colors.onPrimary,
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
            )

            CoroutineScope(Dispatchers.Main).launch {

                val countDownTimer = object: CountDownTimer(
                    1000,
                    1000
                ) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        onNavControllerNavigate()
                    }
                }

                countDownTimer.start()
            }
        }
    }
}