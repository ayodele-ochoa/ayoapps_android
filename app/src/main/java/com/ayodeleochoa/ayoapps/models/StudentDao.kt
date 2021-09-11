package com.ayodeleochoa.ayoapps.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao
{
    @Query("SELECT * FROM student")
    fun getAll(): MutableList<Student>
    //  fun getAll(): Flow<List<Student>>

    @Query("SELECT * FROM student WHERE studentID IN (:studentIDs)")
    fun loadAllByIds(studentIDs: IntArray): Flow<MutableList<Student>>

    @Query("SELECT * FROM student WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Student

    @Query("SELECT * FROM student ORDER BY last_name ASC")
    fun getLastNameOrdered(): Flow<MutableList<Student>>

    @Query("SELECT * FROM student ORDER BY first_name ASC")
    fun getFirstNameOrdered(): Flow<MutableList<Student>>

    @Query("SELECT * FROM student ORDER BY grade ASC")
    fun getGradeOrdered(): Flow<MutableList<Student>>

    @Insert
    fun insertAll(vararg students: Student)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(student: Student)

    @Delete
    fun delete(student: Student)

    @Query("DELETE FROM student")
    fun deleteAll()



}