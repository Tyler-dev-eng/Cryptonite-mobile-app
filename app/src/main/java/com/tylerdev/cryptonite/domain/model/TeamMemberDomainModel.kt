package com.tylerdev.cryptonite.domain.model

data class TeamMemberDomainModel(
    val id: String,
    val name: String,
    val position: String? = null
)
