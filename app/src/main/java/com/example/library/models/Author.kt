package com.example.library.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Author")
data class Author(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "age")
    var age: Int,

    @ColumnInfo(name = "nationality")
    var nationality: String

) : Parcelable