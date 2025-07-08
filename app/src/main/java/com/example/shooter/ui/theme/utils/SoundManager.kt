package com.example.shooter.ui.theme.utils


import android.content.Context
import android.media.SoundPool
import com.example.shooter.R

class SoundManager(context: Context) {
    private val soundPool = SoundPool.Builder().setMaxStreams(5).build()

    private val fireSound = soundPool.load(context, R.raw.fire, 1)
    private val hitSound = soundPool.load(context, R.raw.hit, 1)
    private val bonusSound = soundPool.load(context, R.raw.bonus, 1)

    fun playFire() {
        soundPool.play(fireSound, 1f, 1f, 0, 0, 1f)
    }

    fun playHit() {
        soundPool.play(hitSound, 1f, 1f, 0, 0, 1f)
    }

    fun playBonus() {
        soundPool.play(bonusSound, 1f, 1f, 0, 0, 1f)
    }
}