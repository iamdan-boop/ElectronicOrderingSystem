package com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.composables.CartProductItemCard
import com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.viewmodel.CartListEvent
import com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.viewmodel.CartListViewModel
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.CheckoutEvent
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.CheckoutViewModel
import com.sti.sticanteen.presentation.destinations.CheckoutScreenDestination
import com.sti.sticanteen.ui.theme.Purple700
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun CartScreen(
    viewModel: CartListViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CartTopAppBar(
                isCloseVisible = viewModel.cartState.value.selectedProducts.isNotEmpty(),
                onClose = { viewModel.onEvent(CartListEvent.ClearSelectedProducts) }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.cartState.value.selectedProducts.isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            checkoutViewModel.onEvent(
                                CheckoutEvent.SetSelectedProducts(
                                    selectedProducts = viewModel.cartState.value.selectedProducts,
                                )
                            )
                            viewModel.onEvent(CartListEvent.ClearSelectedProducts)
                            delay(1300L)
                            navigator.navigate(CheckoutScreenDestination)
                        }
                    },
                    modifier = Modifier.padding(
                        bottom = 50.dp
                    ),
                    backgroundColor = Purple700,
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Checkout"
                    )
                }
            }
        },
    ) {
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            viewModel.cartState.value.products.map { cartProduct ->
                item {
                    CartProductItemCard(
                        cartProduct = cartProduct,
                        isSelected = viewModel.cartState.value
                            .selectedProducts.contains(cartProduct),
                        onClick = { viewModel.onEvent(CartListEvent.ProductClick(cartProduct)) },
                        onLongClick = { cartProduct ->
                            viewModel.onEvent(
                                CartListEvent.ProductLongClick(
                                    product = cartProduct
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CartTopAppBar(
    isCloseVisible: Boolean,
    onClose: () -> Unit
) {
    val appBarHorizontalPadding = 4.dp
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .width(72.dp - appBarHorizontalPadding)

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                ) {
                    AnimatedVisibility(
                        visible = isCloseVisible,
                        enter = expandHorizontally(),
                        exit = shrinkVertically(),
                    ) {
                        IconButton(
                            onClick = { onClose() },
                            enabled = true,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Back",
                            )
                        }
                    }
                }
            }
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            text = "Cart"
                        )
                    }
                }
            }
        }
    }
}