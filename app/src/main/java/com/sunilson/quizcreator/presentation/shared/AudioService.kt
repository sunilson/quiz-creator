package com.sunilson.quizcreator.presentation.shared

import android.app.Application
import android.media.MediaPlayer
import android.media.SoundPool
import com.sunilson.quizcreator.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioService @Inject constructor(val application: Application) {

    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private val correctSound: Int
    private val wrongSound: Int
    private var soundsLoaded: Boolean = false
    private val mediaPlayers: Stack<MediaPlayer> = Stack()

    init {
        correctSound = soundPool.load(application, R.raw.correct, 1)
        wrongSound = soundPool.load(application, R.raw.wrong, 1)
        soundPool.setOnLoadCompleteListener { soundPool, i, i2 ->
            soundsLoaded = true
        }
    }

    fun playErrorSound() {
        if (soundsLoaded) soundPool.play(wrongSound, 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun playCorrectSound() {
        if (soundsLoaded) soundPool.play(correctSound, 1.0f, 1.0f, 0, 0, 1.0f)
    }

    fun playWonSound() {
        val mediaPlayer = MediaPlayer.create(application, R.raw.applause)
        mediaPlayers.push(mediaPlayer)
        mediaPlayer.start()
    }

    fun playLostSound() {
        val mediaPlayer = MediaPlayer.create(application, R.raw.booing)
        mediaPlayer.setVolume(0.6f, 0.6f)
        mediaPlayers.push(mediaPlayer)
        mediaPlayer.start()
    }

    fun cancelPlayingSounds() {
        while (mediaPlayers.size > 0) {
            val mediaPlayer = mediaPlayers.pop()
            mediaPlayer.release()
        }
    }
}