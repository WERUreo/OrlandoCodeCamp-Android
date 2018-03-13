package com.werureo.orlandocodecamp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter
import com.werureo.orlandocodecamp.R
import com.werureo.orlandocodecamp.models.ScheduleSection
import com.werureo.orlandocodecamp.models.Session
import kotlinx.android.synthetic.main.session_list_item.view.*
import java.lang.Exception

class SessionsAdapter(
        private val context: Context,
        private val scheduleSections: List<ScheduleSection>
) : SimpleSectionedAdapter<SessionsAdapter.ViewHolder>() {

    override fun getSectionHeaderTitle(section: Int) = scheduleSections[section].timeslot

    override fun getSectionCount() = scheduleSections.count()

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.session_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCountForSection(section: Int) = scheduleSections[section].sessions.count()

    override fun onBindItemViewHolder(holder: ViewHolder?, section: Int, position: Int) {
        holder?.bind(scheduleSections[section].sessions[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(session: Session?) {
            itemView?.sessionName?.text = session?.name
            itemView?.speakerName?.text = session?.speaker
            itemView?.roomNumber?.text = context.getString(
                    R.string.track_room_number,
                    session?.track?.name,
                    session?.track?.roomNumber)
            Picasso.get()
                    .load(session?.speakerImageUrl)
                    .resize(75, 75)
                    .centerCrop()
                    .into(itemView?.speakerImage, object: Callback {
                        override fun onSuccess() {
                            val imageBitmap = (itemView?.speakerImage?.drawable as BitmapDrawable).bitmap
                            val imageDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBitmap)
                            imageDrawable.isCircular = true
                            imageDrawable.cornerRadius = Math.max(imageBitmap.width, imageBitmap.height) / 2.0f
                            itemView.speakerImage.setImageDrawable(imageDrawable)
                        }

                        @SuppressLint("LogNotTimber")
                        override fun onError(e: Exception?) {
                            Log.e("test", "Unable to create circular image")
                        }

                    })
        }
    }
}