package com.example.demo;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class ProgressBarAnimation extends Animation {
    private CircularProgressIndicator indicator;
    private float from;
    private float  to;

    public ProgressBarAnimation(CircularProgressIndicator progressBar, float from, float to) {
        super();
        this.indicator = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        indicator.setProgress((int) value);
    }

}