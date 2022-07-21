package com.sti.sticanteen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.navigation.navigate
import com.sti.sticanteen.presentation.MainScreen
import com.sti.sticanteen.presentation.appCurrentDestinationAsState
import com.sti.sticanteen.presentation.destinations.LoginScreenDestination
import com.sti.sticanteen.ui.theme.STiCanteenTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            STiCanteenTheme {
                MainScreen()
            }
        }
    }
}