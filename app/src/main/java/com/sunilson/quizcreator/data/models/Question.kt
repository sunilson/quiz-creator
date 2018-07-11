package com.sunilson.quizcreator.data.models

data class Question(val id: String, val text: String, val answers: Array<Answer>)