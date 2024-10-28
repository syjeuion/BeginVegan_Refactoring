package com.beginvegan.data.mapper.core

import com.beginvegan.data.model.core.BaseResponse
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.core.BasicResult

class BaseMapper: Mapper<BaseResponse, BasicResult> {
    override fun mapFromEntity(type: BaseResponse): BasicResult {
        return BasicResult(
            check = type.check,
            message = type.information.message
        )
    }
}