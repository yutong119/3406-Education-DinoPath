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