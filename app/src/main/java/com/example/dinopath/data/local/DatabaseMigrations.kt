package com.example.dinopath.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(
        database: SupportSQLiteDatabase,
    ) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS favourite_specimens (
                specimenId TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                period TEXT NOT NULL,
                diet TEXT NOT NULL,
                description TEXT NOT NULL,
                savedAt INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(
        database: SupportSQLiteDatabase,
    ) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS specimen_cache (
                queryTitle TEXT NOT NULL PRIMARY KEY,
                displayTitle TEXT NOT NULL,
                summary TEXT NOT NULL,
                imageUrl TEXT,
                cachedAt INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }
}