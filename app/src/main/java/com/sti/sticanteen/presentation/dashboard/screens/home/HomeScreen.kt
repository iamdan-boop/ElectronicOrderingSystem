package com.sti.sticanteen.presentation.dashboard.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.sti.sticanteen.R
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.presentation.dashboard.composables.SearchBar
import com.sti.sticanteen.presentation.dashboard.screens.home.composables.ProductItemCard
import com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel.BEVERAGES_TYPE_KEY
import com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel.DRINK_TYPE_KEY
import com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel.HomeViewModel
import com.sti.sticanteen.presentation.dashboard.screens.home.viewmodel.SNACK_TYPE_KEY
import com.sti.sticanteen.presentation.destinations.ProductDetailScreenDestination
import com.sti.sticanteen.presentation.destinations.ProductScreenDestination


@Composable
@Destination
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val searchString = remember { mutableStateOf("") }
    val scrollableState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(scrollableState)
    ) {
        SearchBar(
            value = searchString.value,
            onValueChange = { query -> searchString.value = query },
            placeHolder = stringResource(R.string.search_here),
            isReadOnly = true,
            enabled = false,
            onClick = {
                navController.navigate(ProductScreenDestination)
            }
        )
        Box(modifier = Modifier.height(50.dp))
        HomeSection(
            headerText = "Beverages",
            products = viewModel.homeState.value.products[BEVERAGES_TYPE_KEY]
                ?: emptyList(),
            navController = navController
        )
        Box(modifier = Modifier.height(20.dp))
        HomeSection(
            headerText = "Drinks",
            products = viewModel.homeState.value.products[DRINK_TYPE_KEY]
                ?: emptyList(),
            navController = navController
        )
        Box(modifier = Modifier.height(20.dp))
        HomeSection(
            headerText = "Snacks",
            products = viewModel.homeState.value.products[SNACK_TYPE_KEY]
                ?: emptyList(),
            navController = navController
        )
        Box(modifier = Modifier.height(60.dp))
    }
}


@Composable
fun HomeSection(
    headerText: String,
    products: List<Product>,
    navController: NavController
) {
    Column {
        Text(
            text = headerText,
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.W800,
                fontSize = 24.sp
            )
        )
        if (products.isEmpty()) Box {}
        else LazyRow(modifier = Modifier.fillMaxWidth()) {
            products.map { product ->
                item {
                    ProductItemCard(
                        product = product,
                        onClick = {
                            navController.navigate(
                                ProductDetailScreenDestination(
                                    product = product
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}