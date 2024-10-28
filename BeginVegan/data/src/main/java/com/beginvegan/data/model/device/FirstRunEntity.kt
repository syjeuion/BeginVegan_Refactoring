package com.beginvegan.data.model.device

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "first_run")
data class FirstRunEntity(
    @PrimaryKey val id: Int = 1,
    val isFirstRun: Boolean
)