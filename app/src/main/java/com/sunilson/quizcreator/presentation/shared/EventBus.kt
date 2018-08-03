package com.sunilson.quizcreator.presentation.shared

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {

    val reload_questions_subject: PublishSubject<Any> = PublishSubject.create()

    fun subscribeToChannel(channel: EventChannel): Observable<Any> {
        return when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reload_questions_subject.hide()
            }
        }
    }

    fun publishToChannel(channel: EventChannel, data: Any?) {
        when (channel) {
            EventChannel.RELOAD_QUESTIONS -> {
                reload_questions_subject.onNext(data ?: true)
            }
        }
    }
}

enum class EventChannel {
    RELOAD_QUESTIONS
}