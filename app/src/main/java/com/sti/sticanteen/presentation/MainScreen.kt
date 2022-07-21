package com.sti.sticanteen.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.sti.sticanteen.MainViewModel
import com.sti.sticanteen.presentation.destinations.DashboardScreenDestination
import com.sti.sticanteen.presentation.destinations.LoadScreenDestination
import com.sti.sticanteen.presentation.destinations.LoginScreenDestination
import com.sti.sticanteen.utils.AuthenticationState


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()

    val startRoute =
        if (viewModel.isLoggedIn == AuthenticationState.UNAUTHORIZED)
            LoginScreenDestination else LoadScreenDestination

    Log.i("startRoute", startRoute.route)

    DestinationsNavHost(
        engine = engine,
        navController = navController,
        navGraph = NavGraphs.root,
        startRoute = startRoute
    )
    ShowWhenLoggedOut(viewModel = viewModel, navController = navController)
}


@Composable
fun ShowWhenLoggedOut(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val currentDestination by navController.appCurrentDestinationAsState()
    val isLoggedIn by viewModel.isLoggedInState.collectAsState()

    Log.i("currentDestination", "${currentDestination?.route}")
    Log.i("isLoggedInState", "$isLoggedIn")

    if (isLoggedIn == AuthenticationState.UNAUTHORIZED && currentDestination != LoginScreenDestination) {
        navController.navigate(LoginScreenDestination) {
            launchSingleTop = true
        }
        return
    }

    if (isLoggedIn == AuthenticationState.LOGGED_IN && (currentDestination == LoadScreenDestination || currentDestination == LoginScreenDestination)) {
        navController.navigate(DashboardScreenDestination) {
            launchSingleTop = true
        }
    }
}