package com.sti.sticanteen.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.sti.sticanteen.presentation.NavGraphs
import com.sti.sticanteen.presentation.dashboard.composables.BottomNavigationBar
import com.sti.sticanteen.presentation.destinations.HomeScreenDestination


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun DashboardScreen() {
    val navController = rememberNavController()
    val navHostEngine = rememberNavHostEngine()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBarItem.values().forEach { navBarItem ->
                if (navBarItem.direction.route == route) {
                    BottomNavigationBar(
//                        modifier = Modifier.clip(
//                            RoundedCornerShape(
//                                topStart = 10.dp,
//                                topEnd = 10.dp
//                            )
//                        ),
                        navController = navController,
                    )
                }
            }
        }
    ) {
        DestinationsNavHost(
            navController = navController,
            engine = navHostEngine,
            navGraph = NavGraphs.root,
            startRoute = HomeScreenDestination
        )
    }
}
