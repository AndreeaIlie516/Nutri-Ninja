package com.andreeailie.tracker_presentation.search

import com.andreeailie.tracker_domain.model.TrackableFood

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableFood: List<TrackableFoodUiState> = emptyList()
)