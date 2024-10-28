package com.beginvegan.data.model.alarms

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAlarmResponse(
    @Json(name = "check")
    val check: Boolean,
    @Json(name = "information")
    val information: AlarmListDto
)
@JsonClass(generateAdapter = true)
data class AlarmListDto(
    @Json(name = "unreadAlarmResList")
    val unreadAlarmResList: List<AlarmDto>,
    @Json(name = "readAlarmResList")
    val readAlarmResList: List<AlarmDto>
)
@JsonClass(generateAdapter = true)
data class AlarmDto (
    @Json(name = "alarmId")
    val alarmId: Int,
    @Json(name = "alarmType")
    val alarmType: String,
    @Json(name = "content")
    val content: String,
    @Json(name = "itemId")
    val itemId: Int?,
    @Json(name = "createdDate")
    val createdDate: String,
    @Json(name = "isRead")
    val isRead: Boolean
)