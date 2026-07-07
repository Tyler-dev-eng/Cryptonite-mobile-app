package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class Links(
    val explorer: List<String>? = null,
    val facebook: List<String>? = null,
    val reddit: List<String>? = null,
    @param:Json(name = "source_code") val sourceCode: List<String>? = null,
    val website: List<String>? = null,
    val youtube: List<String>? = null
)