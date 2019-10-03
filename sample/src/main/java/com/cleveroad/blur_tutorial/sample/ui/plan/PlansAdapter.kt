package com.cleveroad.blur_tutorial.sample.ui.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.models.PlanModel
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource

class PlansAdapter(context: Context) : RecyclerView.Adapter<PlansAdapter.PlanHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val data = mutableListOf<PlanModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PlanHolder.newInstance(inflater, parent)

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PlanHolder, position: Int) {
        holder.bind(data[position])
    }

    fun addAllNotify(newData: List<PlanModel>) {
        data.run {
            clear()
            addAll(newData)
        }
        notifyDataSetChanged()
    }

    class PlanHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup?) =
                    PlanHolder(inflater.inflate(R.layout.item_plan, parent, false))
        }

        private val ivPlan = view.findViewById<ImageView>(R.id.ivPlan)
        private val tvPlanTitle = view.findViewById<TextView>(R.id.tvPlanTitle)
        private val tvPlanQuantity = view.findViewById<TextView>(R.id.tvPlanQuantity)

        fun bind(item: PlanModel) = item.run {
            ivPlan.imageResource = imageRes
            tvPlanTitle.text = name
            tvPlanTitle.textColorResource = titleColorRes
            tvPlanQuantity.text = quantity
        }
    }
}