package com.dicoding.mydicodingevent.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Event")
@Parcelize
data class  EventEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var mediaCover: String? = null,
    @ColumnInfo("bookmarked")
    var isBookmarked: Boolean
) : Parcelable