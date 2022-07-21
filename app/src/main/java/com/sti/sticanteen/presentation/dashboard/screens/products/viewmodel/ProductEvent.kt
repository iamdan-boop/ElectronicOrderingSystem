package com.sti.sticanteen.presentation.dashboard.screens.products.viewmodel

import com.sti.sticanteen.data.network.entity.Tag

sealed class ProductEvent {
    data class SearchQueryStringChanged(
        val query: String
    ) : ProductEvent()


    data class OnTagSelected(
        val tag: Tag
    ) : ProductEvent()

    object SearchProduct : ProductEvent()

    object GetProducts : ProductEvent()

    object ClearSearchBar : ProductEvent()
}