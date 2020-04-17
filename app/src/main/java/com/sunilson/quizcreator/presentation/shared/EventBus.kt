package com.sunilson.quizcreator.presentation.shared

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus () {

    private val reload_questions_subject: PublishSubject<Any> = PublishSubject.create()
    private val reload_categories_subject: PublishSubject<Any> = PublishSubject.create()

    fun subscribeToChannel(channel: EventChannel): Observable<Any> {
        return when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reload_questions_subject.hide()
            }
            EventChannel.RELOAD_CATEGORIES -> {
                reload_categories_subject.hide()
            }
        }
    }

    fun publishToChannel(channel: EventChannel, data: Any?) {
        when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reload_questions_subject.onNext(data ?: true)
            }
            EventChannel.RELOAD_CATEGORIES -> {
                reload_categories_subject.onNext(data ?: true)
            }
        }
    }
}

enum class EventChannel {
    RELOAD_QUESTIONS, RELOAD_CATEGORIES
}