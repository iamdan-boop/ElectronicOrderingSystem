package com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sti.sticanteen.data.network.entity.getProductType
import com.sti.sticanteen.presentation.dashboard.screens.cart.checkout.viewmodel.ProductCheckout
import com.sti.sticanteen.ui.theme.Purple700
import com.sti.sticanteen.utils.Constants


@Composable
fun CheckoutCardItem(
    checkoutProduct: ProductCheckout,
    onIncrement: (ProductCheckout) -> Unit,
    onDecrement: (ProductCheckout) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.White),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = 5.dp,
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.baseUrl}/storage/${checkoutProduct.product.id}/${checkoutProduct.product.imageUrl.first().fileName}")
                    .crossfade(true)
                    .build(),
                contentDescription = checkoutProduct.product.productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
            )
            CheckoutProductContent(
                checkoutProduct = checkoutProduct,
                onIncrement = onIncrement,
                onDecrement = onDecrement
            )
        }
    }
}


@Composable
fun CheckoutProductContent(
    checkoutProduct: ProductCheckout,
    onIncrement: (ProductCheckout) -> Unit,
    onDecrement: (ProductCheckout) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Box(modifier = Modifier.width(15.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = checkoutProduct.product.productName,
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Price: P ${checkoutProduct.product.price}",
                    maxLines = 1,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.W800,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                )
            }
            Box(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = "Type:",
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 15.sp
                    )
                )
                Box(modifier = Modifier.width(10.dp))
                Text(
                    text = checkoutProduct.product.getProductType(),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W800,
                        color = Color.DarkGray,
                        fontSize = 15.sp
                    )
                )
            }
            Box(modifier = Modifier.height(10.dp))
            CheckoutCardControls(
                currentQuantity = checkoutProduct.quantity,
                onIncrement = { onIncrement(checkoutProduct) },
                onDecrement = { onDecrement(checkoutProduct) }
            )
        }
    }
}


@Composable
fun CheckoutCardControls(
    currentQuantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onDecrement() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.height(30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Purple700
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Clear,
                tint = Color.White,
                contentDescription = "Add Quantity"
            )
        }
        Text(
            text = currentQuantity.toString(),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.W800,
                color = Color.DarkGray,
                fontSize = 15.sp
            )
        )
        Button(
            onClick = { onIncrement() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.height(30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Purple700
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = Color.White,
                contentDescription = "Minus Quantity"
            )
        }
    }
}