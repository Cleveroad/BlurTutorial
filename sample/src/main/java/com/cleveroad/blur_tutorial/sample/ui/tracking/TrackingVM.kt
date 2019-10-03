package com.cleveroad.blur_tutorial.sample.ui.tracking

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.blur_tutorial.sample.ui.base.BaseVM
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class TrackingVM(application: Application) : BaseVM(application) {

    companion object {
        private const val INITIAL_TIMER = 3200000L
        private const val INITIAL_DISTANCE = 3600L
        private const val INITIAL_CALORIES = 169

        private const val PACE_MIN = 3
        private const val PACE_MAX = 6

        private const val TIMER_PERIOD = 1000L
        private const val DISTANCE_PERIOD = 3000L
        private const val PACE_PERIOD = 2000L
    }

    val timerLD = MutableLiveData<Long>()

    val distanceLD = MutableLiveData<Long>()

    val caloriesLD = MutableLiveData<Long>()

    val paceLD = MutableLiveData<Int>()

    fun startTimer() {
        Flowable.interval(TIMER_PERIOD, TimeUnit.MILLISECONDS)
                .map { INITIAL_TIMER + it * TIMER_PERIOD }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timerLD.value = it
                }.addSubscription()
    }

    fun trackDistance() {
        Flowable.interval(DISTANCE_PERIOD, TimeUnit.MILLISECONDS)
                .map { INITIAL_DISTANCE + it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    distanceLD.value = it
                }.addSubscription()
    }

    fun trackCalories() {
        Flowable.interval(DISTANCE_PERIOD, TimeUnit.MILLISECONDS)
                .map { INITIAL_CALORIES + it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    caloriesLD.value = it
                }.addSubscription()
    }

    fun trackPace() {
        Flowable.interval(PACE_PERIOD, TimeUnit.MILLISECONDS)
                .map { Random.nextInt(PACE_MIN, PACE_MAX) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    paceLD.value = it
                }.addSubscription()
    }
}