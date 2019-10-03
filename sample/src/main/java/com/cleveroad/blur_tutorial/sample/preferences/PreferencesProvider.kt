package com.cleveroad.blur_tutorial.sample.preferences

import android.content.Context
import android.content.SharedPreferences
import com.cleveroad.blur_tutorial.sample.SampleApp

object PreferencesProvider {

    private const val PREFS_NAME = "SamplePrefs"

    private const val HOME_TUTORIAL_KEY = "isHomeTutorialAlreadyShown"
    private const val TRACKING_TUTORIAL_KEY = "isTrackingTutorialAlreadyShown"
    private const val PLAN_TUTORIAL_KEY = "isPlanTutorialAlreadyShown"

    private val preferences: SharedPreferences = SampleApp.instance
            .applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isHomeTutorialAlreadyShown: Boolean
        get() = preferences.getBoolean(HOME_TUTORIAL_KEY, false)
        set(value) {
            preferences.edit()
                    .putBoolean(HOME_TUTORIAL_KEY, value)
                    .apply()
        }

    var isTrackingTutorialAlreadyShown: Boolean
        get() = preferences.getBoolean(TRACKING_TUTORIAL_KEY, false)
        set(value) {
            preferences.edit()
                    .putBoolean(TRACKING_TUTORIAL_KEY, value)
                    .apply()
        }

    var isPlanTutorialAlreadyShown: Boolean
        get() = preferences.getBoolean(PLAN_TUTORIAL_KEY, false)
        set(value) {
            preferences.edit()
                    .putBoolean(PLAN_TUTORIAL_KEY, value)
                    .apply()
        }
}