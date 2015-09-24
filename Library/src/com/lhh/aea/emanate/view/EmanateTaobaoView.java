package com.lhh.aea.emanate.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lhh.aea.emanate.R;
import com.lhh.aea.emanate.core.EmanateCore;
import com.lhh.aea.emanate.core.EmanateUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;


/**
 * Created by linhonghong on 15/9/24.
 */
public class EmanateTaobaoView extends RelativeLayout{

    private static final String TAG = "FavorLayout";

    private static final int DRAWABLE_DEFAULT = 0;

    private Interpolator mInterpolatorLine ;
    private Interpolator mInterpolatorAcc ;
    private Interpolator mInterpolatorDce ;
    private Interpolator mInterpolatorAccdec ;
    private Interpolator[] mInterpolators ;

    private int mHeight;//控件的高度
    private int mWidth;//空间的宽度

    private Context mContext;

    private LayoutParams mLayoutParams;


    private Drawable mDrawable;

    private Random mRandom = new Random();

    private int mDrawableHeight;
    private int mDrawableWidth;
    private float mX,mY,mEndX,mEndY;
    private int mScreenWidth,mScreenHeight;

    public EmanateTaobaoView(Context context) {
        super(context);
        mContext = context;
    }

    public EmanateTaobaoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        mInterpolatorLine = new LinearInterpolator();
        mInterpolatorAccdec = new AccelerateDecelerateInterpolator();
        mInterpolatorAcc = new AccelerateInterpolator();
        mInterpolatorDce = new DecelerateInterpolator();

        setDrawable(null);

        mInterpolators = new Interpolator[4];
        mInterpolators[0] = mInterpolatorLine;
        mInterpolators[1] = mInterpolatorAcc;
        mInterpolators[2] = mInterpolatorDce;
        mInterpolators[3] = mInterpolatorAccdec;

    }

    /**
     * 设置起始位置
     * @param x
     * @param y
     */
    public void setEmanateLoc(int x,int y , int endX, int endY){
        mX = x;
        mY = y;
        mEndX = endX;
        mEndY = endY;
        onMeasureEmanate();
    }

    /**
     * 获得屏幕尺寸
     */
    private void getScreenSize(){
        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
    }
    /**
     * 计算相对位置
     */
    private void onMeasureEmanate(){
        getScreenSize();
        mX = mWidth - (mScreenWidth - mX);
        mY = mHeight - (mScreenHeight - mY);
        mEndX = mWidth - (mScreenWidth - mEndX);
        mEndY = mHeight - (mScreenHeight - mEndY);
    }

    /**
     * 设置动画drawable
     * @param drawable
     */
    public void setDrawable(Drawable drawable){
        if(drawable != null){
            mDrawable = drawable.getConstantState().newDrawable();
        }else {
            //初始化显示的图片
            mDrawable = getResources().getDrawable(R.drawable.default_gift_color);
        }

        mDrawableHeight = mDrawable.getIntrinsicHeight();
        mDrawableWidth = mDrawable.getIntrinsicWidth();

        mLayoutParams = new LayoutParams(mDrawableWidth, mDrawableHeight);
    }

    /**
     *
     * @param drawable
     * @param drawableWidth
     * @param drawableHeight
     */
    public void setDrawable(Drawable drawable, int drawableWidth, int drawableHeight){
        if(drawable != null){
            mDrawable = drawable.getConstantState().newDrawable();
        }else {
            //初始化显示的图片
            mDrawable = getResources().getDrawable(R.drawable.default_gift_color);
        }

        mDrawableHeight = (int) EmanateUtils.getRawSize(TypedValue.COMPLEX_UNIT_PX, drawableHeight,mContext);
        mDrawableWidth = (int) EmanateUtils.getRawSize(TypedValue.COMPLEX_UNIT_PX, drawableWidth, mContext);

        mLayoutParams = new LayoutParams(mDrawableWidth, mDrawableHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算该控件本身的高宽
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 启动发散
     */
    public void onStartEmanate() {

        ImageView imageView = new ImageView(getContext());

        imageView.setImageDrawable(mDrawable);

        if(mX == 0.0f || mY == 0.0f){
            //默认为底部居中
            mLayoutParams.addRule(CENTER_HORIZONTAL, TRUE);
            mLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        }

        imageView.setLayoutParams(mLayoutParams);

        ViewHelper.setX(imageView,mX);
        ViewHelper.setY(imageView,mY);

        addView(imageView);


        Log.v(TAG, "lhh - add child count" + getChildCount());

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();

    }

    private Animator getAnimator(View target){
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(mInterpolators[mRandom.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target,"scaleX", 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target,"scaleY", 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha,scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }
    private ValueAnimator getBezierValueAnimator(View target) {

        EmanateCore evaluator = new EmanateCore(getPointF(2),getPointF(1));

        ValueAnimator animator = ValueAnimator.ofObject(evaluator,
                new PointF(mX,mY),
                new PointF(mEndX,mEndY));
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(1000);
        return animator;
    }

    /**
     * 获取间断点
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();

        if(mEndX > mX){
            pointF.x = (mEndX - mX) / 2 ;
        }else{
            pointF.x = (mX - mEndX) /2 ;
        }

        if(mEndY > mY){
            pointF.y = (mEndY - mY) / 2 ;
        }else{
            pointF.y = (mY - mEndY) / 2 ;
        }

        return pointF;
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            PointF pointF = (PointF) animation.getAnimatedValue();

            ViewHelper.setX(target,pointF.x);
            ViewHelper.setY(target,pointF.y);

            //开始旋转~！！！超级风怒！凸

            ViewHelper.setRotation(target, 360 * animation.getAnimatedFraction());

            //开始透明！隐身，超级闪现！
            ViewHelper.setAlpha(target,1.2f - animation.getAnimatedFraction());

            //开始变小，BKB开启！勇士，到这里我已经放了一套大招了！
            ViewHelper.setScaleX(target, 1.3f -animation.getAnimatedFraction());
            ViewHelper.setScaleY(target, 1.3f -animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            removeView((target));
            Log.v(TAG, "lhh - remove child count"+getChildCount());
        }
    }
}
