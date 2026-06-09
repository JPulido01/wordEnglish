package com.example.wordenglish.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [WordEntity::class], version = 2, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE words ADD COLUMN ipa TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN examples TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN synonyms TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN antonyms TEXT")
            }
        }
    }
}
