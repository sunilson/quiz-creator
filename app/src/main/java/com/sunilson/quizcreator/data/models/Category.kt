package com.sunilson.quizcreator.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sunilson.quizcreator.presentation.shared.baseClasses.AdapterElement
import java.util.*

@Entity(tableName = "category")
class Category(@PrimaryKey var id: String = UUID.randomUUID().toString(), var name: String = "", @Ignore var questionCount: Int = 0) :
    AdapterElement {
    override val compareByString: String
        get() = id
}