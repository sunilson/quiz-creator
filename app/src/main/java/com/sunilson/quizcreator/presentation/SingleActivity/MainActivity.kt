package com.sunilson.quizcreator.presentation.SingleActivity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.annotation.AnimRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.HomeFragment
import com.sunilson.quizcreator.presentation.shared.REQUEST_AUDIO_INTENT
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

interface SpeechRecognizingActivity {
    fun checkAudioPermissions() : Boolean
    fun startListening(cb: (String) -> Unit)
    fun stopListening()
}

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, SpeechRecognizingActivity {

    @Inject
    lateinit var quizViewModel: QuizViewModel

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var speechRecognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)
        replaceFragment(HomeFragment.newInstance())
    }

    fun replaceFragment(fragment: BaseFragment,
                        @AnimRes enterAnimation: Int? = null,
                        @AnimRes exitAnimation: Int? = null,
                        @AnimRes reenterAnimation: Int? = null,
                        @AnimRes returnAnimation: Int? = null,
                        addToBackStack: Boolean = true) {
        var transaction = supportFragmentManager.beginTransaction()
        if (enterAnimation != null && exitAnimation != null && reenterAnimation != null && returnAnimation != null) {
            transaction = transaction.setCustomAnimations(enterAnimation, exitAnimation, reenterAnimation, returnAnimation)
        }
        transaction = transaction.replace(R.id.main_activity_frame_layout, fragment)
        if (addToBackStack) {
            transaction = transaction.addToBackStack(fragment.hashCode().toString())
        }

        quizViewModel.reset()
        transaction.commit()
    }

    fun pop() = supportFragmentManager.popBackStack()

    override fun startListening(cb: (String) -> Unit) {
        if(!checkAudioPermissions()) return
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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this, "App required access to audio", Toast.LENGTH_SHORT).show()
                }

                requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_INTENT)
                false
            }
        } else {
            true
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}
