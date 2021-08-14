package com.vonander.japancvcameraapp.presentation.ui.photo

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vonander.japancvcameraapp.domain.data.DataState
import com.vonander.japancvcameraapp.domain.model.SearchTagsResult
import com.vonander.japancvcameraapp.domain.model.Tag
import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.interactors.SearchTags
import com.vonander.japancvcameraapp.interactors.UploadPhoto
import com.vonander.japancvcameraapp.presentation.ui.PhotoEvent
import com.vonander.japancvcameraapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoViewViewModel @Inject constructor(
    private val uploadPhoto: UploadPhoto,
    private val searchTags: SearchTags
) : ViewModel() {

    val tags: MutableState<List<Tag>> = mutableStateOf(listOf())
    val loading = mutableStateOf(false)
    val snackbarMessage = mutableStateOf("")
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
                    searchTags(
                        event.id,
                        event.completion
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
        }
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