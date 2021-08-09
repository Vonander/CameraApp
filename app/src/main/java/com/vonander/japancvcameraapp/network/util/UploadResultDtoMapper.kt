package com.vonander.japancvcameraapp.network.util

import com.vonander.japancvcameraapp.domain.model.UploadResult
import com.vonander.japancvcameraapp.domain.util.DomainMapper
import com.vonander.japancvcameraapp.network.model.UploadResultDto

class UploadResultDtoMapper : DomainMapper<UploadResultDto, UploadResult> {

    override fun mapToDomainModel(model: UploadResultDto): UploadResult {
        return UploadResult(
            result = model.result,
            status = model.status
        )
    }

    override fun mapFromDomainModel(domainModel: UploadResult): UploadResultDto {
        return UploadResultDto(
            result = domainModel.result,
            status = domainModel.status
        )
    }
}