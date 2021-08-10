package com.vonander.japancvcameraapp.network.util

import com.vonander.japancvcameraapp.domain.model.Tags
import com.vonander.japancvcameraapp.domain.util.DomainMapper
import com.vonander.japancvcameraapp.network.model.SearchTagsDto

class SearchTagsDtoMapper : DomainMapper<SearchTagsDto, Tags> {
    override fun mapToDomainModel(model: SearchTagsDto): Tags {
        return Tags(
            result = model.result,
            status = model.status
        )
    }

    override fun mapFromDomainModel(domainModel: Tags): SearchTagsDto {
        return SearchTagsDto(
            result = domainModel.result,
            status = domainModel.status
        )
    }

    fun toDomainList(initial: List<SearchTagsDto>): List<Tags> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Tags>): List<SearchTagsDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}