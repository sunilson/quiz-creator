package com.sunilson.quizcreator.presentation.MainActivity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.widget.Toast
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseActivity
import com.sunilson.quizcreator.presentation.shared.REQUEST_AUDIO_INTENT
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

class MainActivity : BaseActivity(), HasSupportFragmentInjector, SpeechRecognizingActivity {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var speechRecognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager.adapter = ViewPagerAdapter(supportFragmentManager)
        viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                navigation.menu.getItem(p0).isChecked = true
            }
        })
        viewpager.offscreenPageLimit = 2

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.all_questions_fragment -> viewpager.currentItem = 0
                R.id.statistics_fragment -> viewpager.currentItem = 1
                R.id.quiz_fragment -> viewpager.currentItem = 2
            }

            true
        }

        //setSupportActionBar(toolbar)
        //title = getString(R.string.app_name)
        //replaceFragment(HomeFragment.newInstance())
    }

    /*
    fun replaceFragment(fragment: BaseFragment,
                        @AnimRes enterAnimation: Int? = R.anim.slide_in_from_right,
                        @AnimRes exitAnimation: Int? = R.anim.slide_out_to_left,
                        @AnimRes reenterAnimation: Int? = R.anim.slide_in_from_left,
                        @AnimRes returnAnimation: Int? = R.anim.slide_out_to_right,
                        addToBackStack: Boolean = true) {
        var transaction = supportFragmentManager.beginTransaction()

        if (enterAnimation != null && exitAnimation != null && reenterAnimation != null && returnAnimation != null) {
            transaction = transaction.setCustomAnimations(enterAnimation, exitAnimation, reenterAnimation, returnAnimation)
        }

        transaction = transaction.replace(R.id.main_activity_frame_layout, fragment)
        if (addToBackStack) {
            transaction = transaction.addToBackStack(fragment.hashCode().toString())
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(fragment !is HomeFragment)

        quizViewModel.reset()
        transaction.commit()
    }

    fun pop() {
        supportFragmentManager.popBackStack()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
      */

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}
