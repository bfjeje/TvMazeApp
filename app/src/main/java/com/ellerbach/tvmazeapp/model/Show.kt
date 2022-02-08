package com.ellerbach.tvmazeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Show constructor(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val genres: List<String>,
    val schedule: Schedule,
    val image: Image,
    val summary: String? = ""
) : Serializable

data class Image(
    val medium: String?,
    val original: String?
) : Serializable

data class Schedule(
    val time: String?,
    val days: List<String?>
) : Serializable

