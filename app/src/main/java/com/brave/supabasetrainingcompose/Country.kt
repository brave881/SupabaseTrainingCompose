package com.brave.supabasetrainingcompose

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: Int? = 0,
    val name: String?,
    val user_id: String?,
    val capital: String?,
)
