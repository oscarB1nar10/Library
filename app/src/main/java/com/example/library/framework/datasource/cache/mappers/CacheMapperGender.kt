package com.example.library.framework.datasource.cache.mappers

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.util.EntityMapper
import com.example.library.models.GenderCacheEntity
import javax.inject.Inject

class CacheMapperGender
@Inject
constructor(): EntityMapper<GenderCacheEntity, GenderModel>{

    fun entityListToGenderList(entities: List<GenderCacheEntity>): List<GenderModel>{
        val notes: ArrayList<GenderModel> = ArrayList()
        for(entity in entities){
            notes.add(mapFromEntity(entity))
        }
        return notes
    }

    fun genderListToEntityList(notes: List<GenderModel>): List<GenderCacheEntity>{
        val entities: ArrayList<GenderCacheEntity> = ArrayList()
        for(note in notes){
            entities.add(mapToEntity(note))
        }
        return entities
    }

    override fun mapFromEntity(entity: GenderCacheEntity): GenderModel {
        return GenderModel(
            pk = entity.pk,
            name = entity.name,
            description = entity.description,
            created_at = entity.created_at,
            updated_at = entity.updated_at
        )
    }

    override fun mapToEntity(domainModel: GenderModel): GenderCacheEntity {
        return GenderCacheEntity(
            pk = domainModel.pk,
            name = domainModel.name,
            description = domainModel.description,
            created_at = domainModel.created_at,
            updated_at = domainModel.updated_at
        )
    }
}