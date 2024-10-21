package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlistTracks WHERE trackId IN (:tracksIds)")
    fun getTracksByIds(tracksIds: List<String>): Flow<List<PlaylistTrackEntity>>

    @Query("DELETE FROM playlistTracks WHERE trackId = (:trackId) AND created_date IS NULL")
    suspend fun deleteTrack(trackId: String)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}