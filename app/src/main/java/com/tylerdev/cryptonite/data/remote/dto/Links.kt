package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class Links(
    val explorer: List<String> = emptyList(),
    val facebook: List<String> = emptyList(),
    val reddit: List<String> = emptyList(),
    @param:Json(name = "source_code") val sourceCode: List<String> = emptyList(),
    val website: List<String> = emptyList(),
    val youtube: List<String> = emptyList()
)