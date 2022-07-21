package com.sti.sticanteen.presentation.dashboard.screens.products.viewmodel

import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.entity.Tag

data class ProductState(
    val isLoading: Boolean = true,
    val hasLoadedCachedData: Boolean = false,
    val products: List<Product> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val selectedTags: List<Tag> = emptyList(),
    val stateMessage: String = "",
    val searchQuery: String = ""
)