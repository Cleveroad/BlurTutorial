package com.cleveroad.blur_tutorial.sample.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.models.ActivityModel
import com.cleveroad.blur_tutorial.sample.ui.base.BaseVM
import io.reactivex.Single

class HomeVM(application: Application) : BaseVM(application) {

    val activitiesLD = MutableLiveData<List<ActivityModel>>()

    fun getActivities() =
            Single.just(createActivitiesMocks())
                    .subscribe({
                        activitiesLD.value = it
                    }, {})
                    .addSubscription()

    private fun createActivitiesMocks() =
            listOf(ActivityModel("Press", R.color.red, R.drawable.ic_press_header),
                    ActivityModel("Pedal", R.color.blue, R.drawable.ic_fitness_velo),
                    ActivityModel("Treadmill", R.color.dark_yellow, R.drawable.ic_fitness_run),
                    ActivityModel("Run", R.color.purple, R.drawable.ic_run))
}