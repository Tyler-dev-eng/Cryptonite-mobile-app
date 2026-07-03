package com.tylerdev.cryptonite.data.remote.dto

import com.squareup.moshi.Json

data class CoinDetailDto(
    val description: String,
    @param:Json(name = "development_status") val developmentStatus: String,
    @param:Json(name = "first_data_at") val firstDataAt: String,
    @param:Json(name = "hardware_wallet") val hardwareWallet: Boolean,
    @param:Json(name = "hash_algorithm") val hashAlgorithm: String,
    val id: String,
    @param:Json(name = "is_active") val isActive: Boolean,
    @param:Json(name = "is_new") val isNew: Boolean,
    @param:Json(name = "last_data_at") val lastDataAt: String,
    val links: Links,
    @param:Json(name = "links_extended") val linksExtended: List<LinksExtended>,
    val logo: String,
    val message: String,
    val name: String,
    @param:Json(name = "open_source") val openSource: Boolean,
    @param:Json(name = "org_structure") val orgStructure: String,
    @param:Json(name = "proof_type") val proofType: String,
    val rank: Int,
    @param:Json(name = "started_at") val startedAt: String,
    val symbol: String,
    val tags: List<Tag>,
    val team: List<TeamMember>,
    val type: String,
    @param:Json(name = "whitepaper") val whitePaper: Whitepaper
)