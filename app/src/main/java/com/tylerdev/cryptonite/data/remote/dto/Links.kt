package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class Links(
    val explorer: List<String>,
    val facebook: List<String>,
    val reddit: List<String>,
    @param:Json(name = "source_code") val sourceCode: List<String>,
    val website: List<String>,
    val youtube: List<String>
)