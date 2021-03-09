package com.example.library.framework.presentation.book_gender

import com.example.library.business.domain.model.GenderModel
import com.example.library.util.DateUtil

fun createGender(id: Int = -1, name: String, description: String): GenderModel{
    return GenderModel(
        pk = id,
        name = name,
        description = description,
        created_at = DateUtil.getCurrentTimestamp(),
        updated_at = DateUtil.getCurrentTimestamp()
    )
}

fun genderToUpdate(name: String, description: String, currentGender: GenderModel): GenderModel{
    return GenderModel(
        pk = currentGender.pk,
        name = name,
        description = description,
        created_at = currentGender.created_at,
        updated_at = DateUtil.getCurrentTimestamp()
    )
}