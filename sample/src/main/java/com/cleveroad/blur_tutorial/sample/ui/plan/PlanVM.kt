package com.cleveroad.blur_tutorial.sample.ui.plan

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.models.PlanModel
import com.cleveroad.blur_tutorial.sample.ui.base.BaseVM
import io.reactivex.Single

class PlanVM(application: Application) : BaseVM(application) {

    val plansLD = MutableLiveData<List<PlanModel>>()

    fun getPlans() {
        Single.just(createPlanMocks())
                .subscribe({
                    plansLD.value = it
                }, {} )
                .addSubscription()
    }

    private fun createPlanMocks() =
            listOf(PlanModel("Plank", "10:00 min", R.color.dark_yellow, R.drawable.ic_plank),
                    PlanModel("Press", "50 quantity", R.color.red, R.drawable.ic_press),
                    PlanModel("Dumbbells", "100 quantity", R.color.red,  R.drawable.ic_dumbbells),
                    PlanModel("Squats", "100 quantity", R.color.blue, R.drawable.ic_squats),
                    PlanModel("Plank", "5:00 min", R.color.dark_yellow, R.drawable.ic_plank),
                    PlanModel("Dumbbells", "70 quantity", R.color.red, R.drawable.ic_dumbbells))
}