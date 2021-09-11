package com.ayodeleochoa.ayoapps.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Student::class), version = 1)
abstract class AppDatabase : RoomDatabase()
{

    abstract fun studentDao(): StudentDao
}