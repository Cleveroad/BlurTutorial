package com.cleveroad.blur_tutorial.sample.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.sample.R
import com.cleveroad.blur_tutorial.sample.extensions.setDrawableColorRes
import com.cleveroad.blur_tutorial.sample.models.ActivityModel
import org.jetbrains.anko.imageResource

class ActivitiesAdapter(context: Context) : RecyclerView.Adapter<ActivitiesAdapter.ActivityHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val data = mutableListOf<ActivityModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ActivityHolder.newInstance(inflater, parent)

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        holder.bind(data[position])
    }

    fun addAllNotify(newData: List<ActivityModel>) {
        data.run {
            clear()
            addAll(newData)
        }
        notifyDataSetChanged()
    }

    class ActivityHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup?) =
                    ActivityHolder(inflater.inflate(R.layout.item_activity, parent, false))
        }

        private val tvActivityName = view.findViewById<TextView>(R.id.tvActivityName)
        private val ivActivityPic = view.findViewById<ImageView>(R.id.ivActivityPic)

        fun bind(item: ActivityModel) = item.run {
            tvActivityName.text = name
            ivActivityPic.imageResource = icon
            itemView.setDrawableColorRes(background)
        }
    }
}