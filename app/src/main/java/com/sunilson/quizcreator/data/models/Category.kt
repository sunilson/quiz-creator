package com.sunilson.quizcreator.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sunilson.quizcreator.presentation.shared.BaseClasses.AdapterElement
import java.util.*

@Entity(tableName = "category")
class Category(@PrimaryKey var id: String = UUID.randomUUID().toString(), var name: String = "") : AdapterElement {
    override val compareByString: String
        get() = id
}