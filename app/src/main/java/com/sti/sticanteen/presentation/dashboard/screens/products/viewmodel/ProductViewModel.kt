package com.sti.sticanteen.presentation.dashboard.screens.products.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.entity.Tag
import com.sti.sticanteen.domain.repository.ProductRepository
import com.sti.sticanteen.domain.repository.TagRepository
import com.sti.sticanteen.utils.Constants.serverError
import com.sti.sticanteen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val tagRepository: TagRepository
) : ViewModel() {


    private val _state = mutableStateOf(ProductState())
    val productState = _state

    private val _stateMessage = Channel<String>()
    val stateMessage = _stateMessage.receiveAsFlow()

    private val originalProductsState = mutableListOf<Product>()

    private var searchJob: Job? = null
    private var getProductJob: Job? = null


    init {
        getProducts()
        getProductTags()
        savedStateHandle.get<List<Tag>>(PRODUCT_SELECTED_FILTER_TAG)?.let { savedStateTags ->
            Log.i(PRODUCT_SELECTED_FILTER_TAG, "$savedStateTags")
            _state.value = _state.value.copy(
                selectedTags = savedStateTags
            )
        }
        savedStateHandle.get<String>(PRODUCT_QUERY_TAG)?.let { savedStateQuery ->
            Log.i(PRODUCT_QUERY_TAG, savedStateQuery)
            _state.value = _state.value.copy(searchQuery = savedStateQuery)
            searchProduct(savedStateQuery, true)
        }
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.SearchProduct -> searchProduct(_state.value.searchQuery, true)
            is ProductEvent.SearchQueryStringChanged -> searchProduct(event.query, false)
            is ProductEvent.OnTagSelected -> onTagSelected(event.tag)
            ProductEvent.GetProducts -> getProducts()
            ProductEvent.ClearSearchBar -> {
                _state.value = _state.value.copy(
                    searchQuery = "",
                    products = originalProductsState.toList(),
                )
            }
        }
    }


    private fun searchProduct(
        query: String,
        shouldSearchRemote: Boolean
    ) {
        if (query.isBlank()) return
        _state.value = _state.value.copy(searchQuery = query)
        savedStateHandle.set(PRODUCT_QUERY_TAG, _state.value.searchQuery)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            productRepository.searchProducts(
                query = query,
                shouldSearchRemote = shouldSearchRemote
            ).collect { searchResource ->
                when (searchResource) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = searchResource.data ?: emptyList()
                        )
                        _stateMessage.send(searchResource.message ?: serverError)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = !_state.value.hasLoadedCachedData,
                            hasLoadedCachedData = (searchResource.data != null) && searchResource.data.isNotEmpty(),
                            products = searchResource.data ?: emptyList()
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = searchResource.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }


    private fun getProducts() {
        getProductJob?.cancel()
        getProductJob = viewModelScope.launch {
            productRepository.getProducts().collect { productState ->
                when (productState) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            stateMessage = productState.message ?: serverError,
                            products = productState.data ?: emptyList()
                        )

                        _stateMessage.send(productState.message ?: serverError)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = !_state.value.hasLoadedCachedData,
                            hasLoadedCachedData = productState.data != null,
                            products = productState.data ?: emptyList()
                        )
                        setNewOriginalProductCopy(productState.data ?: emptyList())
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = productState.data ?: emptyList()
                        )
                        setNewOriginalProductCopy(productState.data ?: emptyList())
                    }
                }
            }
        }
    }


    private fun getProductTags() = viewModelScope.launch {
        tagRepository.getTags().collect { tagResource ->
            when (tagResource) {
                is Resource.Error -> _state.value = _state.value.copy(
                    tags = tagResource.data ?: emptyList()
                )
                is Resource.Loading -> return@collect
                is Resource.Success -> _state.value = _state.value.copy(
                    tags = tagResource.data ?: emptyList()
                )
            }
        }
    }


    private fun onTagSelected(tag: Tag) {
        if (_state.value.selectedTags.isNotEmpty() &&
            !_state.value.selectedTags.contains(tag)
        ) {
            mutateListTags { mutableTags ->
                mutableTags.add(tag)
            }
            return
        }

        if (_state.value.selectedTags.contains(tag)) {
            mutateListTags { mutableTags ->
                mutableTags.remove(tag)
            }
            return
        }

        mutateListTags { mutableTags ->
            mutableTags.add(tag)
        }
    }


    private fun setNewOriginalProductCopy(products: List<Product>) {
        originalProductsState.removeAll { true }
        originalProductsState.addAll(products)
    }


    private fun mutateListTags(
        onMutateList: (tags: MutableList<Tag>) -> Unit,
    ) {
        val selectedTags =
            _state.value.selectedTags.toMutableList()
                .also { mutableTags -> onMutateList(mutableTags) }
        Log.i(TAG, "mutatedTags: $selectedTags")

        if (selectedTags.isEmpty()) {
            _state.value = _state.value.copy(
                products = originalProductsState.toList(),
                selectedTags = emptyList()
            )
            savedStateHandle.set(PRODUCT_SELECTED_FILTER_TAG, emptyList<Tag>())
            return
        }

        val filteredProducts = originalProductsState.filter { product ->
            product.tags!!.containsAll(
                selectedTags
            )
        }
        Log.i(TAG, "$filteredProducts")
        _state.value = _state.value.copy(
            products = filteredProducts,
            selectedTags = selectedTags.toList(),
        )
        savedStateHandle.set(PRODUCT_SELECTED_FILTER_TAG, selectedTags)
    }


    companion object {
        const val PRODUCT_SELECTED_FILTER_TAG = "PRODUCT_SELECTED_FILTER"
        const val PRODUCT_QUERY_TAG = "searchQuery"
        const val TAG = "PRODUCT_VIEWMODEL"
    }
}