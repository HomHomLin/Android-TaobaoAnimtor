package com.lhh.aea.emanate.core;

import android.graphics.PointF;

import com.nineoldandroids.animation.TypeEvaluator;

/**
 * Created by linhonghong on 15/6/30.
 */
public class EmanateCore implements TypeEvaluator<PointF> {


    private PointF pointF1;
    private PointF pointF2;

    public EmanateCore(PointF pointF1,PointF pointF2){
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }
    @Override
    public PointF evaluate(float time, PointF startValue,
                           PointF endValue) {

        float timeLeft = 1.0f - time;

        PointF point = new PointF();

        PointF point0 = (PointF)startValue;

        PointF point3 = (PointF)endValue;

        point.x = timeLeft * timeLeft * timeLeft * (point0.x)
                + 3 * timeLeft * timeLeft * time * (pointF1.x)
                + 3 * timeLeft * time * time * (pointF2.x)
                + time * time * time * (point3.x);

        point.y = timeLeft * timeLeft * timeLeft * (point0.y)
                + 3 * timeLeft * timeLeft * time * (pointF1.y)
                + 3 * timeLeft * time * time * (pointF2.y)
                + time * time * time * (point3.y);
        return point;
    }
}
