package com.vonander.japancvcameraapp.network.util

import com.vonander.japancvcameraapp.domain.model.SearchTagsResult
import com.vonander.japancvcameraapp.domain.util.DomainMapper
import com.vonander.japancvcameraapp.network.model.SearchTagsDto

class SearchTagsDtoMapper : DomainMapper<SearchTagsDto, SearchTagsResult> {
    override fun mapToDomainModel(model: SearchTagsDto): SearchTagsResult {
        return SearchTagsResult(
            result = model.result,
            status = model.status
        )
    }

    override fun mapFromDomainModel(domainModel: SearchTagsResult): SearchTagsDto {
        return SearchTagsDto(
            result = domainModel.result,
            status = domainModel.status
        )
    }

    fun toDomainList(initial: List<SearchTagsDto>): List<SearchTagsResult> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<SearchTagsResult>): List<SearchTagsDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}