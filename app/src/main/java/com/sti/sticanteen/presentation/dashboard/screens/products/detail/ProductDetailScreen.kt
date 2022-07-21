package com.sti.sticanteen.presentation.dashboard.screens.products.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.ramcosta.composedestinations.annotation.Destination
import com.sti.sticanteen.R
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.presentation.dashboard.screens.products.detail.viewmodel.ProductDetailUiState
import com.sti.sticanteen.presentation.dashboard.screens.products.detail.viewmodel.ProductDetailViewModel
import com.sti.sticanteen.ui.theme.Purple700
import com.sti.sticanteen.utils.Constants


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    product: Product
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current


    LaunchedEffect(key1 = viewModel, block = {
        viewModel.uiState.collect { productUiState ->
            when (productUiState) {
                is ProductDetailUiState.Error ->
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.failed_add_to_cart))
                ProductDetailUiState.Loading -> return@collect
                ProductDetailUiState.Success ->
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.success_add_to_cart))
            }
        }
    })

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            ProductDetailBottomBar(
                onAddToCart = { viewModel.addProductToCart(product.id) },
                product = product,
                isLoading = viewModel.productDetailState.value.isLoading
            )
        }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Constants.baseUrl}/storage/${product.id}/${product.imageUrl.first().fileName}")
                .crossfade(true)
                .build(),
            contentScale = ContentScale.FillHeight,
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxSize()
        )
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductDetailBottomBar(
    onAddToCart: () -> Unit,
    isLoading: Boolean,
    product: Product
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 16.dp,
        modifier = Modifier
            .height(250.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = product.productName,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "P ${product.price}",
                    maxLines = 1,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.W800,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                )
            }
            Text(
                text = "Tags:",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.W800,
                    fontSize = 16.sp
                )
            )
            FlowRow(
                mainAxisSize = SizeMode.Wrap,
                modifier = Modifier.fillMaxWidth()
            ) {
                product.tags?.map { tag ->
                    Chip(
                        onClick = { },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = Purple700,
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Text(
                            text = tag.tag,
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W600,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            if (isLoading) Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            } else TextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Blue,
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = { onAddToCart() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    modifier = Modifier.padding(start = 10.dp),
                    tint = Color.White,
                    contentDescription = "Cart"
                )
                Spacer(modifier = Modifier.weight(0.1f))
                Text(
                    text = "Add to Cart",
                    modifier = Modifier.padding(end = 35.dp),
                    style = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.W800,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.weight(0.1f))
            }
        }
    }
}