package com.sti.sticanteen.presentation.authentication.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sti.sticanteen.R
import com.sti.sticanteen.presentation.authentication.register.viewmodel.RegisterEvent.*
import com.sti.sticanteen.presentation.authentication.register.viewmodel.RegisterViewModel
import com.sti.sticanteen.presentation.composables.CustomTextField
import com.sti.sticanteen.utils.AuthState
import kotlinx.coroutines.flow.collect


@Composable
fun RegisterSheetContent(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel, block = {
        viewModel.authState.collect { authState ->
            when (authState) {
                is AuthState.Authorized -> onSuccess()
                is AuthState.Unauthorized -> return@collect
                is AuthState.UnknownError -> return@collect
            }
        }
    })

    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.White)
    ) {
        Column {
            Text(
                text = stringResource(R.string.register_header),
                style = MaterialTheme.typography.h1.copy(fontSize = 40.sp),
            )
            RegisterInput(
                error = viewModel.state.value.errors.nameError.asString(context),
                value = viewModel.state.value.name,
                hintText = stringResource(id = R.string.tell_us_who_you_are),
                labelText = stringResource(id = R.string.whats_your_name),
                onValueChanged = { name -> viewModel.onEvent(RegisterNameChanged(name)) }
            )
            Box(modifier = Modifier.padding(vertical = 10.dp))
            RegisterInput(
                error = viewModel.state.value.errors.phoneNumberError.asString(context),
                value = viewModel.state.value.phoneNumber,
                hintText = stringResource(id = R.string.tell_us_your_contact_number),
                labelText = stringResource(id = R.string.what_is_your_contact_number),
                onValueChanged = { phoneNumber ->
                    viewModel.onEvent(RegisterPhoneNumberChanged(phoneNumber))
                }
            )
            Box(modifier = Modifier.padding(vertical = 10.dp))
            RegisterInput(
                error = viewModel.state.value.errors.emailError.asString(context),
                value = viewModel.state.value.email,
                hintText = stringResource(id = R.string.hint_email),
                labelText = stringResource(id = R.string.type_email_address),
                onValueChanged = { email ->
                    viewModel.onEvent(RegisterEmailChanged(email))
                }
            )
            Box(modifier = Modifier.padding(vertical = 10.dp))
            RegisterInput(
                error = viewModel.state.value.errors.passwordError.asString(context),
                value = viewModel.state.value.password,
                hintText = stringResource(id = R.string.password_hint),
                labelText = stringResource(id = R.string.type_password_here),
                onValueChanged = { password ->
                    viewModel.onEvent(RegisterPasswordChanged(password))
                }
            )
            Box(modifier = Modifier.padding(vertical = 10.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (viewModel.state.value.isLoading) Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                else Button(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(top = 10.dp),
                    onClick = {
                        viewModel.onEvent(RegisterSubmit)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.sign_up),
                        color = Color.White,
                        style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterInput(
    error: String,
    value: String,
    hintText: String,
    labelText: String,
    onValueChanged: (String) -> Unit,
) {
    CustomTextField(
        hintText = hintText,
        labelText = labelText,
        value = value,
        onValueChanged = { password -> onValueChanged(password) },
    )
    AnimatedVisibility(
        visible = error.isNotBlank(),
        modifier = Modifier.padding(horizontal = 7.dp)
    ) {
        Text(
            text = error,
            fontSize = 14.sp,
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.W600,
                color = Color.Red
            )
        )
    }
}