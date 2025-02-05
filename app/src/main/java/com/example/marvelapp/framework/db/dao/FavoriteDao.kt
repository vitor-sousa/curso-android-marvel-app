package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.DbConstants.FAVORITES_TABLE_NAME
import com.example.marvelapp.framework.db.entitiy.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM $FAVORITES_TABLE_NAME")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM $FAVORITES_TABLE_NAME WHERE id = :characterId")
    suspend fun hasFavorite(characterId: Int): FavoriteEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)
}