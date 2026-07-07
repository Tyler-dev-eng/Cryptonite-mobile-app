package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class Tag(
    @param:Json(name = "coin_counter") val coinCounter: Int? = null,
    @param:Json(name = "ico_counter") val icoCounter: Int? = null,
    val id: String,
    val name: String
)