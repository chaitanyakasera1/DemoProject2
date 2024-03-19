package com.example.demo

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class StationAdapter(private val itemList: List<TextItem>) :
    RecyclerView.Adapter<StationAdapter.StateViewHolder>() {
    val liveData: MutableLiveData<Int> = MutableLiveData<Int>(-10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.text_item_title, parent, false)
            return TitleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.station_item_view, parent, false)
            return ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val station = itemList[position]
        holder.bind(station, position)
    }

    override fun getItemCount(): Int = itemList.size

    inner abstract class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: TextItem, position: Int)
        fun resetLine(view: LineAnimationView) {
            view.setLineLength(0f);
        }

        fun animateLine(view: LineAnimationView) {
            val animator = ObjectAnimator.ofFloat(
                view,
                "lineLength",
                0f,
                view.height.toFloat()
            ) // Start from 0 and end at 1000
            animator.duration = 1000 // Animation duration in milliseconds
            animator.start()
        }

        fun bounceAnimation(view: View) {
            val bounceAnimation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
            view.startAnimation(bounceAnimation)
        }

        fun animateCircularIndicator(view: CircularProgressIndicator) {
            val progressanim = ProgressBarAnimation(view, 0f, 100f)
            progressanim.duration = 1000
            view.startAnimation(progressanim)
        }

        fun animateColor(view: View, fromColor: Int, toColor: Int) {
            val animator = ValueAnimator.ofArgb(fromColor, toColor)
            animator.duration = 200
            animator.addUpdateListener {
                view.setBackgroundColor(toColor)
            }
            animator.start()
        }

        fun changeColor(view: View) {
            view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.black))
        }
    }

    inner class ItemViewHolder(itemView: View) : StateViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        val subTitle: TextView = itemView.findViewById(R.id.subTitle)
        val dot: View = itemView.findViewById<View>(R.id.dot)
        val lineAnimationView: LineAnimationView = itemView.findViewById(R.id.lineAnimationView)
            init {
                liveData.observeForever {
                    Log.e("::", "bind: $position", )
                    if (it == position) {
                        Log.e("::", "matches: $position", )
                        val listener = object : OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            }
                        }
                        itemView.viewTreeObserver.addOnGlobalLayoutListener(listener)
                        bounceAnimation(dot)
                        changeColor(dot)
                        android.os.Handler(Looper.myLooper()!!).postDelayed({
//                                animateColor(dot)
                            animateLine(lineAnimationView)
                        }, 500)
                    }
                }
            }
        override fun bind(item: TextItem, position: Int) {
            title.text = item.name
            subTitle.text = item.subTitle
        }
    }

    inner class TitleViewHolder(itemView: View) : StateViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val progressIndicator: CircularProgressIndicator =
            itemView.findViewById(R.id.progressBar)
        val lineAnimationView: LineAnimationView = itemView.findViewById(R.id.lineAnimationView)

        override fun bind(item: TextItem, position: Int) {
            title.text = item.name
            title.setOnClickListener {
                refreshList()
            }
            val listener = object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    resetLine(lineAnimationView)
                    animateCircularIndicator(progressIndicator)
                    Handler(Looper.myLooper()!!).postDelayed({
                        animateLine(lineAnimationView)
                    }, 500)
                }
            }
            itemView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getItemViewType(position: Int): Int {
        val item: TextItem = itemList.get(position)
        return if (item.isTitle) {
            1
        } else {
            2
        };
    }

    fun refreshList() {
        val new = mutableListOf<TextItem>()
        new.addAll(itemList)
        notifyDataSetChanged()
    }
}

data class TextItem(val name: String, val subTitle: String = "", val isTitle: Boolean = false) {
    // Add other properties as needed
}