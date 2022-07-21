package com.sti.sticanteen.presentation.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun LoadScreen(
//    viewModel: MainViewModel = hiltViewModel(),
//    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()
//    val context = LocalContext.current
//
//    LaunchedEffect(viewModel, context) {
//        viewModel.isLoggedInState.collect { currentState ->
//            when (currentState) {
//                AuthenticationState.LOGGED_IN -> navigator.navigate(DashboardScreenDestination)
//                AuthenticationState.UNAUTHORIZED -> navigator.navigate(LoginScreenDestination)
//                AuthenticationState.UNKNOWN -> scaffoldState.snackbarHostState.showSnackbar(
//                    "Unknown error occur, try restarting the application"
//                )
//            }
//        }
//    }

    Scaffold(
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}