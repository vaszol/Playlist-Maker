package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.LikedTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: LikedTrackEntity)

    @Delete
    suspend fun deleteTrack(track: LikedTrackEntity)

    @Query("SELECT * FROM likedTracks ORDER BY created_date DESC")
    fun getTracks(): Flow<List<LikedTrackEntity>>

    @Query("SELECT trackId FROM likedTracks")
    fun getTracksIds(): List<String>
}