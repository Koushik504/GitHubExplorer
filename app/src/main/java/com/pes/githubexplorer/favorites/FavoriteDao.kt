package com.pes.githubexplorer.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_repositories ORDER BY fullName COLLATE NOCASE")
    fun observeAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_repositories WHERE id = :repositoryId)")
    fun observeIsFavorite(repositoryId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_repositories WHERE id = :repositoryId")
    suspend fun deleteById(repositoryId: Long)

    @Query("DELETE FROM favorite_repositories")
    suspend fun clearAll()
}
