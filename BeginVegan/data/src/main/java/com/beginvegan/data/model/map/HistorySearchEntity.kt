package com.beginvegan.data.model.map

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "historySearch")
data class HistorySearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String
)