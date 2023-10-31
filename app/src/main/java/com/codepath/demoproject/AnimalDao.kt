package com.codepath.demoproject

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animal_table")
    fun getAll(): Flow<List<DisplayAnimal>>

    @Insert
    fun insert(favorite: AnimalEntity)

    @Query("DELETE FROM animal_table")
    fun deleteAll()
}