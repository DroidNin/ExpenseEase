package com.example.expenseease

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val images: MutableList<Bitmap>, private val onDelete: (Int) -> Unit) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val deleteButton: AppCompatButton = itemView.findViewById(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageBitmap(images[position])
        holder.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    override fun getItemCount() = images.size

    fun removeAt(position: Int) {
        images.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, images.size)
    }
}

