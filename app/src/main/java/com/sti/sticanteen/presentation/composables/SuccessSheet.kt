package com.sti.sticanteen.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sti.sticanteen.R

@Composable
fun SuccessSheet(
    referenceNumber: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentScale = ContentScale.Fit,
                contentDescription = "Check"
            )
            Box(modifier = Modifier.height(10.dp))
            Text(
                text = "Order Successfully queued.",
                style = MaterialTheme.typography.h4.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W800,
                )
            )
            Text(
                text = "Reference no. $referenceNumber",
                style = MaterialTheme.typography.h6.copy(
                    fontSize = 12.sp
                )
            )
        }
    }
}