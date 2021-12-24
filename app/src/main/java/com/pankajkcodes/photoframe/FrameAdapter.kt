package com.pankajkcodes.photoframe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FrameAdapter(
    private val context: Context, private val list: List<FrameList>,
    private val onFrameClick: OnFrameClick
) :
    RecyclerView.Adapter<FrameAdapter.FrameViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        return FrameViewHolder(
            LayoutInflater.from(context).inflate(R.layout.frame_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        Glide.with(context).load(list[position].bgFrame).into(holder.imageView)
        holder.imageView.setOnClickListener {
            onFrameClick.frameClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FrameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.frameItem)
    }

}