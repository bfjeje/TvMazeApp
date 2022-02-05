package com.ellerbach.tvmazeapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromList(strings: List<String?>?): String? {
        if (strings == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(strings, type)
    }

    @TypeConverter
    fun toList(string: String?): List<String>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson<List<String>>(string, type)
    }

    @TypeConverter
    fun fromScheduleToString(schedule: Schedule?): String? {
        return gson.toJson(schedule)
    }

    @TypeConverter
    fun fromStringToSchedule(data: String?): Schedule? {
        val scheduleType = object : TypeToken<Schedule>() {
        }.type
        return gson.fromJson(data, scheduleType)
    }

    @TypeConverter
    fun fromImageToString(image: Image?): String? {
        return gson.toJson(image)
    }

    @TypeConverter
    fun fromStringToImage(data: String?): Image? {
        val imageType = object : TypeToken<Image>() {
        }.type
        return gson.fromJson(data, imageType)
    }
}