package com.sti.sticanteen.presentation.dashboard.screens.cart.cart_list.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.data.network.entity.getProductType
import com.sti.sticanteen.ui.theme.Purple700
import com.sti.sticanteen.utils.Constants

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CartProductItemCard(
    cartProduct: Product,
    isSelected: Boolean,
    onLongClick: (Product) -> Unit,
    onClick: (Product) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, if (isSelected) Purple700 else Color.White),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(10.dp)
            .combinedClickable(
                onClick = { onClick(cartProduct) },
                onLongClick = { onLongClick(cartProduct) }
            ),
        elevation = 5.dp,
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.baseUrl}/storage/${cartProduct.id}/${cartProduct.imageUrl.first().fileName}")
                    .crossfade(true)
                    .build(),
                contentDescription = cartProduct.productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
            )
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
                    Text(
                        text = cartProduct.productName,
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = 15.sp
                        )
                    )
                    Box(modifier = Modifier.height(5.dp))
                    Row {
                        Text(
                            text = "Type:",
                            style = MaterialTheme.typography.h6.copy(
                                fontSize = 15.sp
                            )
                        )
                        Box(modifier = Modifier.width(5.dp))
                        Text(
                            text = cartProduct.getProductType(),
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.W800,
                                color = Color.DarkGray,
                                fontSize = 15.sp
                            )
                        )
                    }
                    Box(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Price: P ${100.00}",
                        maxLines = 1,
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.W800,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    )
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        FlowRow(
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            cartProduct.tags?.map { product ->
//                                Chip(
//                                    colors = ChipDefaults.chipColors(
//                                        backgroundColor = Purple700,
//                                        contentColor = Color.White,
//                                    ),
//                                    onClick = { /*TODO*/ }
//                                ) {
//                                    Text(product.tag)
//                                }
//                            }
//                        }
//
//                    }
                }
            }
        }
    }
}