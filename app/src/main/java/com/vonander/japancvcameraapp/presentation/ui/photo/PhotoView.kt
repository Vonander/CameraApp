package com.vonander.japancvcameraapp.presentation.ui.photo

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.datastore.PhotoDataStore
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.*
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun PhotoView(
    viewModel: PhotoViewViewModel,
    onNavControllerNavigate: (String) -> Unit,
) {

    val context = LocalContext.current
    val tags = viewModel.tags.value
    val snackbarMessage = viewModel.snackbarMessage.value
    val takePhotoButtonText = viewModel.takePhotoButtonText.value
    val searchtagsButtonText = viewModel.searchTagsButtonText.value
    val loading = viewModel.loading.value
    val scaffoldState = rememberScaffoldState()

    getLatestStoredPhoto(context, viewModel)

    val photoUri = viewModel.photoUri.value

    JapanCVCameraAppTheme() {

        Scaffold(
            bottomBar = {
                CustomBottomBar(
                    onNavControllerNavigate = onNavControllerNavigate
                )
            },
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
                            if (photoUri.isBlank()) {
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

                    CircularIndeterminateProgressBar(isDisplayed = loading)

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

                    setButtonText(viewModel = viewModel)

                    if (!photoUri.isBlank()) {

                        CustomButton(
                            modifier = Modifier.padding(top = 40.dp),
                            buttonText = searchtagsButtonText,
                            enabled = !loading,
                            onClick = {
                                viewModel.loading.value = true

                                updateTagsList(
                                    viewModel = viewModel,
                                    completion = {
                                        println("updateTagsList complete")
                                    }
                                )
                            }
                        )
                    }

                    CustomButton(
                        buttonText = takePhotoButtonText,
                        modifier = Modifier.padding(top = 40.dp),
                        enabled = !loading,
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

                        viewModel.snackbarMessage.value = ""
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

private fun updateTagsList(
    viewModel: PhotoViewViewModel,
    completion: () -> Unit) {
    val photoUri = viewModel.photoUri.value

    viewModel.onTriggerEvent(
        event = PhotoEvent.UploadPhoto(
            uriString = photoUri,
            completion = { succeeded ->

                if (succeeded) {
                    viewModel.onTriggerEvent(
                        event = PhotoEvent.SearchTags
                    )

                    completion()
                }
            }
        )
    )
}

private fun setButtonText(viewModel: PhotoViewViewModel) {
    if (viewModel.loading.value) {
        viewModel.searchTagsButtonText.value = "Loading..."
        
    } else {
        viewModel.searchTagsButtonText.value = "Search for tags #"

        if (viewModel.photoUri.value.isBlank()) {
            viewModel.takePhotoButtonText.value = "Take Photo"
        } else {
            viewModel.takePhotoButtonText.value = "Take a new Photo"
        }
    }
}

private fun getLatestStoredPhoto(
    context: Context,
    viewModel: PhotoViewViewModel
) {

    viewModel.viewModelScope.launch {
        val dataStore = PhotoDataStore(context)
        viewModel.photoUri.value = dataStore.getPhotoUriString()
    }
}