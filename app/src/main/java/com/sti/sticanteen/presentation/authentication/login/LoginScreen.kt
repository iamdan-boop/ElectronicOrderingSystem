package com.sti.sticanteen.presentation.authentication.login

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sti.sticanteen.R
import com.sti.sticanteen.presentation.authentication.login.viewmodel.LoginEvent.*
import com.sti.sticanteen.presentation.authentication.login.viewmodel.LoginViewModel
import com.sti.sticanteen.presentation.authentication.register.RegisterSheetContent
import com.sti.sticanteen.presentation.composables.CustomTextField
import com.sti.sticanteen.presentation.destinations.DashboardScreenDestination
import com.sti.sticanteen.utils.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.stateChannel.collect { stateMessage ->
            when (stateMessage) {
                is UiText.DynamicString -> {
                    scaffoldState.snackbarHostState.showSnackbar(stateMessage.value)
                }
                is UiText.StringResource -> scaffoldState.snackbarHostState.showSnackbar(
                    stateMessage.asString(context)
                )
            }
        }
    }

    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutine.launch {
            bottomSheetState.animateTo(
                targetValue = ModalBottomSheetValue.Hidden,
                anim = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
            )
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            RegisterSheetContent(
                onSuccess = {
                    coroutine.launch {
                        bottomSheetState.animateTo(
                            targetValue = ModalBottomSheetValue.Hidden,
                            anim = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
                        )
                        delay(500)
                        navigator.navigate(
                            direction = DashboardScreenDestination,
                            builder = {
                                NavOptionsBuilder().apply {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            )
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        LoginBodyContent(
            scaffoldState = scaffoldState,
            viewModel = viewModel,
            bottomSheetState = bottomSheetState,
            coroutine = coroutine
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginBodyContent(
    scaffoldState: ScaffoldState,
    viewModel: LoginViewModel,
    bottomSheetState: ModalBottomSheetState,
    coroutine: CoroutineScope
) {
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_header),
                    style = MaterialTheme.typography.h1.copy(fontSize = 40.sp),
                )
                Text(
                    text = stringResource(R.string.login_header_subtitle),
                    style = MaterialTheme.typography.h6
                )
                Box(modifier = Modifier.height(70.dp))
                CustomTextField(
                    hintText = stringResource(R.string.hint_email),
                    labelText = stringResource(R.string.type_email_address),
                    value = viewModel.state.value.email,
                    onValueChanged = { email ->
                        viewModel.onEvent(LoginEmailChanged(email))
                    },
                )
                Box(modifier = Modifier.height(15.dp))
                CustomTextField(
                    options = KeyboardOptions(keyboardType = KeyboardType.Password),
                    hintText = stringResource(R.string.password_hint),
                    labelText = stringResource(R.string.type_password_here),
                    value = viewModel.state.value.password,
                    onValueChanged = { password ->
                        viewModel.onEvent(
                            LoginPasswordChanged(
                                password
                            )
                        )
                    }
                )
                Text(
                    text = stringResource(R.string.register_here),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            coroutine.launch {
                                if (bottomSheetState.isVisible) {
                                    return@launch bottomSheetState.animateTo(
                                        targetValue = ModalBottomSheetValue.Hidden,
                                        anim = tween(
                                            durationMillis = 700,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                }
                                return@launch bottomSheetState.animateTo(
                                    targetValue = ModalBottomSheetValue.Expanded,
                                    anim = tween(
                                        durationMillis = 700,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        },
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.W600,
                        color = Color.Blue,
                        textAlign = TextAlign.End
                    )
                )
                if (viewModel.state.value.isLoading) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CircularProgressIndicator()
                } else Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(top = 10.dp),
                        onClick = {
                            viewModel.onEvent(LoginSubmit)
                        }
                    ) {
                        Text(
                            stringResource(R.string.sign_in),
                            color = Color.White,
                            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
                        )
                    }
                }
            }
        }
    }
}