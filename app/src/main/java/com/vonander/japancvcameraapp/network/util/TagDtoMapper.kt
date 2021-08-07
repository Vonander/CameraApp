package com.vonander.japancvcameraapp.network.util

import com.vonander.japancvcameraapp.domain.Tag
import com.vonander.japancvcameraapp.domain.util.DomainMapper
import com.vonander.japancvcameraapp.network.model.TagDto

class TagDtoMapper : DomainMapper<TagDto, Tag> {
    override fun mapToDomainModel(model: TagDto): Tag {
        return Tag(
            confidence = model.confidence,
            tag = model.tag
        )
    }

    override fun mapFromDomainModel(domainModel: Tag): TagDto {
        return TagDto(
            confidence = domainModel.confidence,
            tag = domainModel.tag
        )
    }

    fun toDomainList(initial: List<TagDto>): List<Tag> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Tag>): List<TagDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}