package com.beginvegan.data.mapper.alarms

import com.beginvegan.data.model.alarms.AlarmDto
import com.beginvegan.data.model.alarms.AlarmListDto
import com.beginvegan.domain.mapper.Mapper
import com.beginvegan.domain.model.alarms.Alarm
import com.beginvegan.domain.model.alarms.AlarmLists

class AlarmMapper:Mapper<AlarmDto, Alarm> {
    override fun mapFromEntity(type: AlarmDto): Alarm {
        return Alarm(
            alarmId = type.alarmId,
            alarmType = type.alarmType,
            content = type.content,
            itemId = type.itemId,
            createdDate = type.createdDate
        )
    }
    fun fromDtoList(dtoList: List<AlarmDto>):List<Alarm>{
        return dtoList.map { mapFromEntity(it) }
    }
    fun mapToAlarmLists(dtolists: AlarmListDto): AlarmLists {
        return AlarmLists(
            unreadAlarmList = fromDtoList(dtolists.unreadAlarmResList),
            readAlarmList = fromDtoList(dtolists.readAlarmResList)
        )
    }
}