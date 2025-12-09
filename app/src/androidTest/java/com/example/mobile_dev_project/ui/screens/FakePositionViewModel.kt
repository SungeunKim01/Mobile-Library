package com.example.mobile_dev_project.ui.screens

class FakePositionViewModel : PositionViewModel(contentRepository = null) {
    private val positions = mutableMapOf<Int, Float>()

    override suspend fun getScrollPosition(contentId: Int): Float? {
        return positions[contentId] ?:0f
    }

    override fun saveScrollPosition(contentId: Int, scrollPosition: Float) {
        positions[contentId] = scrollPosition
    }
}
