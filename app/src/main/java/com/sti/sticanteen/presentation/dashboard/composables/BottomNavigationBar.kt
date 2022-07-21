package com.sti.sticanteen.presentation.dashboard.composables

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.sti.sticanteen.presentation.dashboard.BottomNavBarItem
import com.sti.sticanteen.ui.theme.Purple700


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    BottomNavigation(
        modifier = modifier,
        elevation = 5.dp,
        backgroundColor = Purple700
    ) {
        BottomNavBarItem.values().forEach { destination ->
            val isCurrentDestinationOnBackstack =
                navController.currentDestination == destination.direction
            BottomNavigationItem(
                selected = currentDestination?.route?.contains(destination.direction.route) == true,
                onClick = {
                    if (isCurrentDestinationOnBackstack) {
                        navController.popBackStack(destination.direction, false)
                        return@BottomNavigationItem
                    }

                    navController.navigate(destination.direction) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.direction.route
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}