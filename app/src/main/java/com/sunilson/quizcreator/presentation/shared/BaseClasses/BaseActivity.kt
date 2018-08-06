package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.hasPermission
import com.sunilson.quizcreator.presentation.shared.REQUEST_AUDIO_INTENT
import dagger.android.AndroidInjection
import javax.inject.Inject

interface SpeechRecognizingActivity {
    fun checkAudioPermissions(): Boolean
    fun startListening(cb: (String) -> Unit)
    fun stopListening()
}

abstract class BaseActivity : AppCompatActivity(), SpeechRecognizingActivity {

    @Inject
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var speechRecognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun startListening(cb: (String) -> Unit) {
        if (!checkAudioPermissions()) return
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}
            override fun onRmsChanged(p0: Float) {}
            override fun onBufferReceived(p0: ByteArray?) {}
            override fun onPartialResults(p0: Bundle?) {}
            override fun onEvent(p0: Int, p1: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onEndOfSpeech() {}
            override fun onError(p0: Int) {}
            override fun onResults(p0: Bundle?) {
                val matches = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) cb(matches[0])
                else cb("")
            }
        })
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }

    override fun checkAudioPermissions(): Boolean {
        return if (hasPermission(android.Manifest.permission.RECORD_AUDIO)) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_INTENT)
            false
        }
    }
}