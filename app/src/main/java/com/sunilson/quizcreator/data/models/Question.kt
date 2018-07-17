package com.sunilson.quizcreator.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.BaseObservable
import android.databinding.ObservableField
import com.sunilson.quizcreator.presentation.shared.BaseClasses.AdapterElement
import java.util.*

@Entity(tableName = "question")
class Question(
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var text: String = "",
        var categoryId: String = "",
        var answers: MutableList<Answer> = mutableListOf(),
        var correctAnswerId: String = "") : BaseObservable(), AdapterElement {

    @Ignore
    override val compareByString: String = id

    @Ignore
    val observableText : ObservableField<String> = object: ObservableField<String>(text) {
        override fun set(value: String?) {
            super.set(value)
            if(value != null) text = value
        }
    }
}