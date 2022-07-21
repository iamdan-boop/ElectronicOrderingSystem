package com.sti.sticanteen.presentation.dashboard.screens.home.composables


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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.sti.sticanteen.data.network.entity.Product
import com.sti.sticanteen.utils.Constants


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductItemCard(
    product: Product,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .padding(10.dp)
            .height(160.dp)
            .width(125.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.baseUrl}/storage/${product.id}/${product.imageUrl.first().fileName}")
                    .crossfade(true)
                    .build(),
                contentDescription = product.productName,
//                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(100.dp)
                    .padding(0.dp)
            )
            Box(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = product.productName,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "P ${product.price}",
                    maxLines = 1,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.W800,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}