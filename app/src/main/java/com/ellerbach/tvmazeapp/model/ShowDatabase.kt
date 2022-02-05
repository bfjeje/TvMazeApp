package com.ellerbach.tvmazeapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ShowDAO {
    @Insert
    suspend fun save(show: Show?): Long

    @get:Query("SELECT * FROM Show")
    val searchAll: LiveData<List<Show?>?>

    @Query("SELECT * FROM Show WHERE id = :id")
    fun searchShow(id: Long): Show?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(shows: List<Show?>?)
}


@Database(entities = [Show::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShowDatabase : RoomDatabase() {
    abstract val showDAO: ShowDAO
}

private lateinit var INSTANCE: ShowDatabase

fun getDatabase(context: Context): ShowDatabase {
    synchronized(ShowDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ShowDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

