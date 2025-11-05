package com.example.mobile_dev_project.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * this is ViewModel fo download book screen
 * -Hold the url text the user enter
 * -expose whether "Add to Library" button should be enabled
 * viewmodel to survive configuration changes (rotation, process recreation, etc) and keep Uui state out of Composables
 */
class DownloadBookViewModel : ViewModel() {

    //backing state for the url text field.
    var url by mutableStateOf("")
    private set

    //enabling the Add button
    //Now, any non blank text enables
    val canAdd: Boolean
    get() = url.isNotBlank()

    // event from Uui when user types into url textfield
    fun onUrlChanged(newUrl: String) {
        url = newUrl
    }

    // event for future milestones when U actually download and add the book
    // so for now, I keep it so the ui can call smt later
    fun onAddClicked() {
        // maybe on M2??? - trigger use case to download and save book, and then navigate back
    }
}