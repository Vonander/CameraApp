package com.vonander.japancvcameraapp.presentation.ui.photo

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.japancvcameraapp.domain.model.Tag
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PhotoViewViewModel @Inject constructor(
    private val uploadPhoto: UploadPhoto,
    private val searchTags: SearchTags
) : ViewModel() {

    val tags: MutableState<List<Tag>> = mutableStateOf(listOf())
    val loading = mutableStateOf(false)
    val snackbarMessage = mutableStateOf("")
    val photoId = mutableStateOf("")
    val photoUri = mutableStateOf("")

    fun onTriggerEvent(event: PhotoEvent) {
        try {
            when(event) {
                is PhotoEvent.UploadPhoto -> {
                    uploadPhoto(
                        event.uriString,
                        event.completion
                    )
                }
                is PhotoEvent.SearchTags -> {
                    searchTags()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
    }

    private fun uploadPhoto(
        uriString: String,
        completion: (Boolean) -> Unit
    ) {

        uploadPhoto.execute(
            uriString = uriString
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let {  uploadResult ->
                photoId.value = uploadResult.result.getValue("upload_id")
                completion(true)
            }

            dataState.error?.let { message ->
                Log.e(TAG, "upload photo error: $message")
                snackbarMessage.value = message
                completion(false)
            }

        }.launchIn(viewModelScope)
    }

    private fun searchTags() {
        searchTags.execute(
            id = photoId.value
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { searchTagsResult ->
                updateTagsList(searchTagsResult.result.getValue("tags"))
            }

            dataState.error?.let { message ->
                Log.e(TAG, "search tags error: $message")
                snackbarMessage.value = message
            }

        }.launchIn(viewModelScope)
    }

    private fun updateTagsList(rawList: List<HashMap<String, Any>>?){
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

        snackbarMessage.value = "New # tags updated!"

        tags.value = updatedList
    }
}