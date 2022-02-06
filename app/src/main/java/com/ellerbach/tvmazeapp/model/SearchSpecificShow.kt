package com.ellerbach.tvmazeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchSpecificShow constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val score: Double,
    val show: Show
)
