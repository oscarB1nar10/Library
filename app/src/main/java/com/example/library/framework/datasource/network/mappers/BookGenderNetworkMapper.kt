package com.example.library.framework.datasource.network.mappers

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.util.EntityMapper
import com.example.library.framework.datasource.network.model.BookGenderNetworkEntity
import javax.inject.Inject

class BookGenderNetworkMapper
@Inject
constructor() : EntityMapper<BookGenderNetworkEntity, GenderModel> {

    fun entityListToGenderList(entities: List<BookGenderNetworkEntity>): List<GenderModel> {
        val notes: ArrayList<GenderModel> = ArrayList()
        for (entity in entities) {
            notes.add(mapFromEntity(entity))
        }
        return notes
    }

    fun genderListToEntityList(notes: List<GenderModel>): List<BookGenderNetworkEntity> {
        val entities: ArrayList<BookGenderNetworkEntity> = ArrayList()
        for (note in notes) {
            entities.add(mapToEntity(note))
        }
        return entities
    }

    override fun mapFromEntity(entity: BookGenderNetworkEntity): GenderModel {
        return GenderModel(
                pk = entity.pk,
                name = entity.name,
                description = entity.description,
                created_at = entity.created_at,
                updated_at = entity.updated_at
        )
    }

    override fun mapToEntity(domainModel: GenderModel): BookGenderNetworkEntity {
        return BookGenderNetworkEntity(
                pk = domainModel.pk,
                name = domainModel.name,
                description = domainModel.description,
                created_at = domainModel.created_at,
                updated_at = domainModel.updated_at
        )
    }

}
