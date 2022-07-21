package com.sti.sticanteen.presentation.dashboard.screens.products.detail.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sti.sticanteen.R
import com.sti.sticanteen.ui.theme.Purple700

@Composable
fun ProductQuantitySheetScreen(
    value: String,
    calculatedPrice: String,
    onValueChange: (String) -> Unit,
    onIncrement: () -> Unit,
    addToCart: () -> Unit,
    isLoading: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .height(230.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.quantity_and_price),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.W800,
                    fontSize = 24.sp
                )
            )
            Box(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    OutlinedTextField(
                        value = value,
                        trailingIcon = trailingIcon,
                        onValueChange = { quantity ->
                            if (quantity.isNotEmpty()) return@OutlinedTextField
                            onValueChange(quantity)
                        },
                    )
                    Text(
                        text = "Price: $calculatedPrice",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.W800,
                            fontSize = 14.sp,
                            color = Color.Red,
                        )
                    )
                }
                Box(modifier = Modifier.width(10.dp))
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Purple700
                    ),
                    modifier = Modifier.height(60.dp),
                    onClick = { onIncrement() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        tint = Color.White,
                        contentDescription = "Add Quantity"
                    )
                }
            }
            Box(modifier = Modifier.height(10.dp))
            if (isLoading) Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                CircularProgressIndicator()
            } else TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Purple700
                ),
                onClick = { addToCart() }
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.button.copy(
                        fontWeight = FontWeight.W800,
                    )
                )
            }
        }
    }
}