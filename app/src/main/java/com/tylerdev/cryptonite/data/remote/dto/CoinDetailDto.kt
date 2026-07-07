package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class CoinDetailDto(
    val description: String? = null,
    @param:Json(name = "development_status") val developmentStatus: String? = null,
    @param:Json(name = "first_data_at") val firstDataAt: String? = null,
    @param:Json(name = "hardware_wallet") val hardwareWallet: Boolean? = null,
    @param:Json(name = "hash_algorithm") val hashAlgorithm: String? = null,
    val id: String,
    @param:Json(name = "is_active") val isActive: Boolean? = null,
    @param:Json(name = "is_new") val isNew: Boolean? = null,
    @param:Json(name = "last_data_at") val lastDataAt: String? = null,
    val links: Links? = null,
    @param:Json(name = "links_extended") val linksExtended: List<LinksExtended>? = null,
    val logo: String? = null,
    val message: String? = null,
    val name: String,
    @param:Json(name = "open_source") val openSource: Boolean? = null,
    @param:Json(name = "org_structure") val orgStructure: String? = null,
    @param:Json(name = "proof_type") val proofType: String? = null,
    val rank: Int? = null,
    @param:Json(name = "started_at") val startedAt: String? = null,
    val symbol: String,
    val tags: List<Tag>? = null,
    val team: List<TeamMember>? = null,
    val type: String? = null,
    @param:Json(name = "whitepaper") val whitePaper: Whitepaper? = null
)