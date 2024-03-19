package com.example.demo

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainIcon: ImageView
    private lateinit var trainPath: View
    private val adapter = StationAdapter(
        listOf(
            TextItem("Glaciers", isTitle = true),
            TextItem("Reading", "Glacier Dynamics"),
            TextItem("Listening", "Secrets of Glaciers"),
            TextItem("Vocabulary", "Sharpen your language skills"),
            TextItem("Reading +", "Global Prespective"),
            TextItem("Vocublary", "Expand your Word Bank"),
            TextItem("Listening +", "Glacial Periods"),
            TextItem("Extra", "Watch and write an analytical paragraph"),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
        recyclerView = findViewById(R.id.recyclerView)
        trainIcon = findViewById(R.id.trainIcon)
        trainPath = findViewById(R.id.trainPath)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Animate train movement
                val startY = 0f // Start position of train icon along Y-axis
                val endY = recyclerView.height.toFloat() // End position of train icon along Y-axis
                val duration =
                    ((endY / 15) * 1_000).toLong() // Duration of animation in milliseconds

                val animator = ObjectAnimator.ofFloat(trainIcon, "translationY", startY, endY)
                animator.duration = duration
                animator.interpolator = LinearInterpolator()
                animator.start()
                Thread {
                    Thread.sleep(1000)
                    updateValue()
                }.start()
            }
        })
    }

    fun updateValue() {
        runOnUiThread {
            adapter.liveData.value = adapter.liveData.value!! + 1;
            Log.e("::", "triggered: ${adapter.liveData.value}")
        }
        Thread.sleep(1000)
        updateValue()
    }
}