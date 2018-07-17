package com.sunilson.quizcreator.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "category")
data class Category(@PrimaryKey var id: String = UUID.randomUUID().toString(), var name: String = "")