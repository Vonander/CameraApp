package com.vonander.japancvcameraapp.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.vonander.japancvcameraapp.R
import com.vonander.japancvcameraapp.navigation.Screen
import com.vonander.japancvcameraapp.presentation.components.CustomBottomBar
import com.vonander.japancvcameraapp.presentation.components.CustomButton
import com.vonander.japancvcameraapp.presentation.components.TagListHeader
import com.vonander.japancvcameraapp.ui.theme.JapanCVCameraAppTheme

@ExperimentalFoundationApi
@Composable
fun PhotoView(
    viewModel: MainViewModel,
    photoUri: String,
    onNavControllerNavigate: (String) -> Unit,
) {
    val tags = viewModel.tags.value

    JapanCVCameraAppTheme {

        Scaffold(
            topBar = {},
            bottomBar = {CustomBottomBar(onNavControllerNavigate)},
            drawerContent = {},
        ) {

            Surface(
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxSize()
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
                            modifier = Modifier.padding(
                                start = 0.dp,
                                top = 0.dp,
                                end = 0.dp,
                                bottom = 20.dp
                            )
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

/*                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        itemsIndexed(
                            items = tags
                        ) { index, tag ->

                            if (index == 0) {
                                TagListHeader(
                                    confidence = tag.confidence,
                                    tag = tag.tag
                                )
                            } else {
                                Text(
                                    text = "${tag.confidence} ${tag.tag}",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }

                            if (index == tags.lastIndex) {
                                CustomButton(
                                    modifier = Modifier.padding(top = 40.dp),
                                    onClick = {
                                        val route = Screen.CameraPreview.route
                                        onNavigationToCameraPreviewScreen(route)
                                    },
                                    buttonText = "Take new photo?"
                                )
                            }
                        }
                    }*/
                    }

                    var takePhotoButtonText = "Take Photo"

                    if(!photoUri.isNullOrBlank()) {

                        CustomButton(
                            modifier = Modifier.padding(top = 40.dp),
                            buttonText = "Search for tags #",
                            onClick = {
                                viewModel.onTriggerEvent(

                                    event = PhotoEvent.debug

/*                                event = PhotoEvent.UploadPhoto(
                                    uriString = photoUri,
                                    completion = { photoId ->

                                        viewModel.onTriggerEvent(
                                            event = PhotoEvent.SearchTags(
                                                id = photoId.data?.result?.getValue("upload_id"),
                                                completion = {

                                                    viewModel.updateTagsList(
                                                        it.data?.result?.getValue("tags")
                                                    )
                                                }
                                            )
                                        )
                                    }
                                )*/
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
            }
        }
    }
}