package com.beginvegan.data.mapper.map

import com.beginvegan.data.model.map.HistorySearchEntity
import com.beginvegan.domain.mapper.DBMapper
import com.beginvegan.domain.model.map.HistorySearch

class HistorySearchMapper: DBMapper<HistorySearch, HistorySearchEntity> {
    override fun mapFromEntity(type: HistorySearch): HistorySearchEntity {
        return HistorySearchEntity(
            id = type.id,
            description = type.description
        )
    }

    override fun entityFromMap(type: HistorySearchEntity): HistorySearch {
        return HistorySearch(
            id = type.id,
            description =  type.description
        )
    }

    fun mapToSearchList(searchList: List<HistorySearchEntity>): List<HistorySearch>{
        return searchList.map { entityFromMap(it) }
    }
}