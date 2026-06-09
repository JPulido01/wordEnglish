package com.example.wordenglish.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [WordEntity::class, WordQueueEntity::class, FavoriteEntity::class],
    version = 4,
    exportSchema = false
)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun wordQueueDao(): WordQueueDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE words ADD COLUMN ipa TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN examples TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN synonyms TEXT")
                db.execSQL("ALTER TABLE words ADD COLUMN antonyms TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS word_queue (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        word TEXT NOT NULL,
                        definition TEXT NOT NULL,
                        ipa TEXT,
                        examples TEXT,
                        synonyms TEXT,
                        antonyms TEXT,
                        queuePosition INTEGER NOT NULL
                    )"""
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS favorites (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        word TEXT NOT NULL,
                        definition TEXT NOT NULL,
                        ipa TEXT,
                        examples TEXT,
                        synonyms TEXT,
                        antonyms TEXT,
                        addedAt INTEGER NOT NULL
                    )"""
                )
            }
        }
    }
}
