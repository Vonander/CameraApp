package com.vonander.japancvcameraapp.presentation.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.SearchTagsResult
import com.vonander.japancvcameraapp.domain.model.Tag
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.TakePhoto
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val takePhoto: TakePhoto,
    private val uploadPhoto: UploadPhoto,
    private val searchTags: SearchTags,
    private @Named("Authorization") val authorization: String,
) : ViewModel() {

    var tags: MutableState<List<Tag>> = mutableStateOf(listOf())
    val screenOrientation = mutableStateOf(1)
    val loading = mutableStateOf(false)

    fun onTriggerEvent(event: PhotoEvent) {
        try {
            when(event) {
                is PhotoEvent.TakePhoto -> {
                    takePhoto(
                        event.imageCapture,
                        event.completion
                    )
                }
                is PhotoEvent.UploadPhoto -> {
                    uploadPhoto(
                        event.uriString,
                        event.completion
                    )
                }
                is PhotoEvent.SearchTags -> {
                    searchTags(
                        event.id,
                        event.completion
                    )
                }

                is PhotoEvent.debug -> {
                    println("okej debug mode enabled")
                    val updatedList: MutableList<Tag> = mutableListOf()

                    for (i in 1..30) {
                        val tag = Tag(
                            confidence = "$i",
                            tag = "#imageTag$i"
                        )

                        updatedList.add(tag)
                    }

                    tags.value = updatedList
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
    }

    private fun takePhoto(
        imageCapture: ImageCapture,
        completion: () -> Unit
    ) {
        takePhoto.execute(imageCapture, completion)
    }

    private fun uploadPhoto(
        uriString: String,
        completion: (DataState<UploadResult>) -> Unit
    ) {
        uploadPhoto.execute(
            uriString = uriString,
            completion = completion
        )
    }

    private fun searchTags(
        id: String?,
        completion: (DataState<SearchTagsResult>) -> Unit
    ) {
        searchTags.executeTest(
            id = id,
            completion = completion
        )
    }

    fun updateTagsList(rawList: List<HashMap<String, Any>>?){
        val updatedList: MutableList<Tag> = mutableListOf()

        rawList?.forEach {
            val confidenceString = it.get("confidence").toString()
            val confidenceFloat = confidenceString.toFloat()
            val confidenceRounded = String.format("%.1f", confidenceFloat)
            val tagString = it.get("tag").toString()

            val tagEdited = tagString.replace("{en=","").dropLast(1)

            val tag = Tag(
                confidence = confidenceRounded,
                tag = tagEdited
            )

            updatedList.add(tag)
        }

        tags.value = updatedList
    }
}