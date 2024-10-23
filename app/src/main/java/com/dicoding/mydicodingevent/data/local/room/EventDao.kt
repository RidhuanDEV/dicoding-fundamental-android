package com.dicoding.mydicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.mydicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM Event ORDER BY id DESC")
    fun getEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM Event where bookmarked = 1")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Update
    fun updateEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: EventEntity)

    @Query("DELETE FROM Event WHERE bookmarked = 0")
    fun deleteAll()

    @Delete
    fun deleteEvent(event: EventEntity)

    @Query("SELECT EXISTS(SELECT * FROM Event WHERE id = :id AND bookmarked = 1)")
    fun isEventBookmarked(id: String): Boolean

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<EventEntity?>
}