package com.sti.sticanteen.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    labelText: String,
    hintText: String? = null,
    options: KeyboardOptions? = null,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.W600,
                color = Color.Blue
            )
        )
        TextField(
            value = value,
            keyboardOptions = options ?: KeyboardOptions.Default,
            onValueChange = onValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            placeholder = {
                hintText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.W400,
                            color = Color.DarkGray
                        )
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Gray.copy(alpha = 0.10f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )
    }
}
