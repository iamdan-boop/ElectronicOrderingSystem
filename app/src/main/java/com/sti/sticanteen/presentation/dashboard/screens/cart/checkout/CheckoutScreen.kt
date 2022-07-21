package com.sti.sticanteen.presentation.dashboard.screens.cart.checkout

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.sti.sticanteen.presentation.composables.SuccessSheet
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.composable.CheckoutCardItem
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.CheckoutEvent
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.CheckoutViewModel
import com.sti.sticanteen.presentation.destinations.CartScreenDestination
import com.sti.sticanteen.ui.theme.Purple700
import com.sti.sticanteen.utils.Resource

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun CheckoutScreen(
    navHostController: NavController
) {
    /*
    * get the parent viewmodel for shared state
    * */
    val parentEntry = remember { navHostController.getBackStackEntry(CartScreenDestination.route) }
    val viewModel: CheckoutViewModel = hiltViewModel(parentEntry)

    val (selectedProducts, isLoading, referenceNumber, subTotal) = viewModel.checkoutState.value
    val scaffoldState = rememberScaffoldState()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    LaunchedEffect(key1 = viewModel, block = {
        viewModel.uiState.collect { referenceResource ->
            when (referenceResource) {
                is Resource.Error -> scaffoldState.snackbarHostState.showSnackbar(referenceResource.message!!)
                is Resource.Loading -> return@collect
                is Resource.Success ->
                    modalBottomSheetState.animateTo(
                        targetValue = ModalBottomSheetValue.Expanded,
                        anim = tween(700, easing = FastOutSlowInEasing)
                    )
            }
        }
    })

    ModalBottomSheetLayout(
        sheetContent = { SuccessSheet(referenceNumber = referenceNumber) },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomNavigation(
                    elevation = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.padding(vertical = 7.dp)) {
                            Text(
                                text = "SubTotal: $subTotal",
                                style = MaterialTheme.typography.h6.copy(
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "Total Items: ${selectedProducts.size}",
                                style = MaterialTheme.typography.h6.copy(
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        if (isLoading) Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        } else TextButton(
                            onClick = { viewModel.onEvent(CheckoutEvent.CheckoutProducts) },
                            modifier = Modifier
                                .height(50.dp)
                                .width(120.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Purple700,
                            ),
                            shape = RoundedCornerShape(5.dp),
                        ) {
                            Text(
                                text = "Checkout",
                                style = MaterialTheme.typography.h6.copy(
                                    fontSize = 14.sp
                                )
                            )
                            Box(modifier = Modifier.padding(start = 10.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Checkout"
                            )
                        }
                    }
                }
            }
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    items(viewModel.checkoutState.value.selectedProducts) { product ->
                        CheckoutCardItem(
                            checkoutProduct = product,
                            onIncrement = { productCheckout ->
                                viewModel.onEvent(
                                    CheckoutEvent.OnIncrementProductQuantity(
                                        productCheckout
                                    )
                                )
                            },
                            onDecrement = { productCheckout ->
                                viewModel.onEvent(
                                    CheckoutEvent.OnDecrementProductQuantity(
                                        productCheckout
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}

