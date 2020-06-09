package com.example.library.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Book")
data class Book(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk")
    var pk: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "editorial")
    var editorial: String,

    @ColumnInfo(name = "bookGenderId")
    val bookGenderId: Int,

    @ColumnInfo(name = "bookAuthorId")
    val bookAuthorId: Int,

    @ColumnInfo(name = "bookOwnerId")
    val bookOwnerId: Int

) : Parcelable