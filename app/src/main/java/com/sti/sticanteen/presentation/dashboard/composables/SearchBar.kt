package com.sti.sticanteen.presentation.dashboard.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    onValueChange: (String) -> Unit,
    value: String,
    placeHolder: String? = null,
    isReadOnly: Boolean = false,
    enabled: Boolean = true,
    hasTrailingIcon: Boolean = false,
    onClick: () -> Unit = {},
    onSearch: () -> Unit = {},
    onTrailingClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = value,
            readOnly = isReadOnly,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.LightGray.copy(0.3f)
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search here..."
                )
            },
            trailingIcon = {
                if (hasTrailingIcon) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable {
                            onTrailingClick()
                        }
                    )
                }
            },
            placeholder = {
                placeHolder?.let { value ->
                    Text(
                        text = value,
                        style = MaterialTheme.typography.h6.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.W400,
                        )
                    )
                }
            },
            onValueChange = { onValueChange(it) }
        )
    }
}