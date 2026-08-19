package com.example.ctq_ascenseur.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.example.ctq_ascenseur.data.dao.AppDao
import com.example.ctq_ascenseur.data.model.*

@Database(
    entities = [
        Elevator::class, 
        Inspection::class, 
        Section::class, 
        ControlPointTemplate::class, 
        InspectionResult::class
    ],
    version = 2,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}

// The expected object that Room will use to construct the database
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

// Room KMP requires a way to create the database builder on each platform
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun getDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .build()
}
