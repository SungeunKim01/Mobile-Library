package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PositionViewModel @Inject constructor(
    private val contentRepository: ContentRepository?
) : ViewModel() {

    open suspend fun getScrollPosition(contentId: Int): Float? {
        return contentRepository?.getScreenPosition(contentId)
    }

    open fun saveScrollPosition(contentId: Int, scrollPosition: Float) {
        viewModelScope.launch {
            contentRepository?.updateScreenPosition(contentId, scrollPosition)
        }
    }
}
