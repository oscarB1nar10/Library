package com.example.library.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class GenderModel(
    var pk: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null
): Parcelable{

    companion object{

        fun getUpdatedAtDate(gender: GenderModel): Date {
            val dateFormat = SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US)
            return dateFormat.parse(gender.updated_at?:"")?: Date()
        }
    }
}