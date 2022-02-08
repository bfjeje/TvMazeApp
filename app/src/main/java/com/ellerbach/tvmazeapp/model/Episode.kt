package com.ellerbach.tvmazeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Episode constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val url: String?,
    val name: String,
    val season: Long,
    val number: Long,
    val type: String?,
    val airdate: String?,
    val airtime: String?,
    val airstamp: String?,
    val runtime: Long?,
    val rating: Rating?,
    val image: Image?,
    val summary: String?,
    val links: Links
) : Serializable

data class Links(
    val self: Self
) : Serializable

data class Self(
    val href: String
) : Serializable

data class Rating(
    val average: Double
) : Serializable