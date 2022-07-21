package com.sti.sticanteen.data.network.entity

data class AuthToken(
    val authToken: String,
    val user: AppUser
)