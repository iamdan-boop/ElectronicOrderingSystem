package com.sti.sticanteen.presentation.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.sti.sticanteen.presentation.destinations.CartScreenDestination
import com.sti.sticanteen.presentation.destinations.DirectionDestination
import com.sti.sticanteen.presentation.destinations.HomeScreenDestination
import com.sti.sticanteen.presentation.destinations.SettingScreenDestination


enum class BottomNavBarItem(
    val direction: DirectionDestination,
    val icon: ImageVector,
) {

    Home(
        direction = HomeScreenDestination,
        icon = Icons.Filled.Home,
    ),

    Cart(
        direction = CartScreenDestination,
        icon = Icons.Filled.ShoppingCart,
    ),

    Orders(
        direction = CartScreenDestination,
        icon = Icons.Filled.List,
    ),

    Settings(
        direction = SettingScreenDestination,
        icon = Icons.Filled.ExitToApp,
    )
}