package com.vonander.japancvcameraapp.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.CustomBottomBar
import com.vonander.japancvcameraapp.presentation.components.CustomButton
import com.vonander.japancvcameraapp.presentation.components.DefaultSnackbar
import com.vonander.japancvcameraapp.presentation.components.TagListHeader
import com.vonander.japancvcameraapp.presentation.ui.photo.PhotoViewViewModel
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun PhotoView(
    viewModel: PhotoViewViewModel,
    photoUri: String,
    onNavControllerNavigate: (String) -> Unit,
) {
    val tags = viewModel.tags.value
    val snackbarMessage = viewModel.snackbarMessage.value
    val scaffoldState = rememberScaffoldState()

    JapanCVCameraAppTheme() {

        Scaffold(
            bottomBar = {
                CustomBottomBar(
                    onNavControllerNavigate = onNavControllerNavigate
                )
            },
            //drawerContent = {},
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {

                    Image(
                        painter = rememberGlidePainter(
                            request =
                            if (photoUri.isNullOrBlank()) {
                                R.drawable.placeholder_image
                            } else {
                                photoUri
                            },
                            previewPlaceholder = R.drawable.placeholder_image,
                        ),
                        contentDescription = "photo taken by device camera",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .requiredSize(width = 300.dp, height = 400.dp)
                            .padding(10.dp)
                            .scale(scaleX = -1f, scaleY = 1f)
                    )

                    if (tags.isNotEmpty()) {

                        TagListHeader(
                            confidence = tags[0].confidence,
                            tag = tags[0].tag
                        )

                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(minSize = 150.dp),
                            modifier = Modifier.padding(bottom = 20.dp)
                        ) {

                            items(tags.size) { index ->

                                if (index != tags.size) {
                                    Text(
                                        text = "${tags[index].confidence}% ${tags[index].tag}",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.onSurface,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    var takePhotoButtonText = "Take Photo"

                    if(!photoUri.isNullOrBlank()) {

                        CustomButton(
                            modifier = Modifier.padding(top = 40.dp),
                            buttonText = "Search for tags #",
                            onClick = {

                                viewModel.snackbarMessage.value = "New # tags updated!"

                                viewModel.onTriggerEvent(

                                    event = PhotoEvent.UploadPhoto(
                                        uriString = photoUri,
                                        completion = { dataState ->

                                            dataState.error?.let { message ->
                                                viewModel.snackbarMessage.value = message
                                            }

                                            dataState.data?.let {
                                                viewModel.onTriggerEvent(
                                                    event = PhotoEvent.SearchTags(
                                                        id = dataState.data.result.getValue("upload_id"),
                                                        completion = { dataState ->
                                                            viewModel.updateTagsList(
                                                                dataState.data?.result?.getValue("tags")
                                                            )
                                                        }
                                                    )
                                                )
                                            }
                                        }
                                    )
                                )
                            }
                        )

                        takePhotoButtonText = "Take a new Photo"
                    }

                    CustomButton(
                        modifier = Modifier.padding(top = 40.dp),
                        buttonText = takePhotoButtonText,
                        onClick = {
                            onNavControllerNavigate(Screen.CameraPreview.route)
                        }
                    )
                }

                if (snackbarMessage.isNotBlank()) {

                    viewModel.viewModelScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar (
                            message = snackbarMessage,
                            actionLabel = "Hide"
                        )
                    }

                    DefaultSnackbar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 50.dp),
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    )
                }
            }
        }
    }
}