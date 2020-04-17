package com.sunilson.quizcreator.presentation.shared

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class EventBus {

    private val reloadQuestionsSubject: PublishSubject<Any> = PublishSubject.create()
    private val reloadCategoriesSubject: PublishSubject<Any> = PublishSubject.create()

    fun subscribeToChannel(channel: EventChannel): Observable<Any> {
        return when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reloadQuestionsSubject.hide()
            }
            EventChannel.RELOAD_CATEGORIES -> {
                reloadCategoriesSubject.hide()
            }
        }
    }

    fun publishToChannel(channel: EventChannel, data: Any?) {
        when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reloadQuestionsSubject.onNext(data ?: true)
            }
            EventChannel.RELOAD_CATEGORIES -> {
                reloadCategoriesSubject.onNext(data ?: true)
            }
        }
    }
}

enum class EventChannel {
    RELOAD_QUESTIONS, RELOAD_CATEGORIES
}