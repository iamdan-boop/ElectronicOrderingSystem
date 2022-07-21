package com.sti.sticanteen.presentation.dashboard.screens.products

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sti.sticanteen.presentation.dashboard.composables.SearchBar
import com.sti.sticanteen.presentation.dashboard.screens.home.composables.ProductItemCard
import com.sti.sticanteen.presentation.dashboard.screens.products.viewmodel.ProductEvent
import com.sti.sticanteen.presentation.dashboard.screens.products.viewmodel.ProductViewModel
import com.sti.sticanteen.presentation.destinations.ProductDetailScreenDestination
import com.sti.sticanteen.ui.theme.Purple700


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
@Destination
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.stateMessage.collect { stateMessage ->
            scaffoldState.snackbarHostState.showSnackbar(stateMessage)
        }
    })

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 10.dp)
        ) {
            SearchBar(
                onValueChange = { query ->
                    viewModel.onEvent(ProductEvent.SearchQueryStringChanged(query = query))
                },
                hasTrailingIcon = true,
                onTrailingClick = { viewModel.onEvent(ProductEvent.ClearSearchBar) },
                value = viewModel.productState.value.searchQuery,
                onSearch = {
                    viewModel.onEvent(ProductEvent.SearchProduct)
                    keyboardController?.hide()
                }
            )
            Text(
                text = "Products",
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 0.dp
                ),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.W800,
                    fontSize = 24.sp
                )
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !viewModel.productState.value.isLoading,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        viewModel.productState.value.tags.map { tag ->
                            item {
                                FilterChip(
                                    selected = viewModel.productState.value.selectedTags.contains(
                                        tag
                                    ),
                                    modifier = Modifier.padding(5.dp),
                                    colors = ChipDefaults.filterChipColors(
                                        disabledBackgroundColor = Color.DarkGray,
                                        selectedBackgroundColor = Purple700,
                                    ),
                                    onClick = { viewModel.onEvent(ProductEvent.OnTagSelected(tag = tag)) }
                                ) {
                                    Text(
                                        text = tag.tag,
                                        style = MaterialTheme.typography.body1.copy(
                                            fontWeight = FontWeight.W600,
                                            fontSize = 14.sp,
                                            color = if (viewModel.productState
                                                    .value.selectedTags
                                                    .contains(tag)
                                            ) Color.White else Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }



            if (viewModel.productState.value.isLoading &&
                !viewModel.productState.value.hasLoadedCachedData
            ) Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalShimmer()
            } else LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp),

                ) {
                val products = viewModel.productState.value.products
                products.map { product ->
                    item {
                        ProductItemCard(
                            product = product,
                            onClick = {
                                navigator.navigate(ProductDetailScreenDestination(product = product))
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LazyVerticalShimmer() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
    ) {
        items(6) {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .height(160.dp)
                    .width(125.dp)
                    .placeholder(
                        visible = true,
                        color = Color.Gray,
                        // optional, defaults to RectangleShape
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        ),
                    ),
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Color.White,
                elevation = 3.dp
            ) {}
        }
    }
}


