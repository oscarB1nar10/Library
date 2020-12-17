package com.example.library.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Gender")
data class GenderCacheEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    var pk: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "updated_at")
    var updated_at: String? = null

) : Parcelable