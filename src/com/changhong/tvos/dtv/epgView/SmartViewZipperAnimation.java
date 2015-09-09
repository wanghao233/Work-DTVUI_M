package com.changhong.tvos.dtv.epgView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SmartViewZipperAnimation extends Animation {   
	private Camera mCamera;      
    private float mFromDegree;   //开始角度
    private float mToDegree;   //结束角度
    private float mTranslateX;   //位移X
    private float mTranslateY;    //位移Y
    private final float mDepthZ;  //深度
    private boolean mReverse;//缩放/扩大
    public SmartViewZipperAnimation(float fromDegree, float toDegree,float tx, float ty,float depth,boolean reverse) {   
        this.mFromDegree = fromDegree;   
        this.mToDegree = toDegree;   
        this.mTranslateX = tx;   
        this.mTranslateY = ty;
        this.mDepthZ =depth;     
        this.mReverse =reverse;
    }   
  
    public void initialize(int width, int height, int parentWidth,int parentHeight) {   
        super.initialize(width, height, parentWidth, parentHeight);   
        mCamera = new Camera(); 

    }   
  
	@Override   
    protected void applyTransformation(float interpolatedTime, Transformation t) {   
        final float FromDegree = mFromDegree;   
        float degrees = FromDegree + (mToDegree - mFromDegree)* interpolatedTime;  
        final float centerX = mTranslateX;   
        final float centerY = mTranslateY;   
        final Matrix matrix = t.getMatrix();   
        mCamera.setLocation(0, 0, mDepthZ);
        mCamera.save();   
        if(!mReverse){//缩小
        	mCamera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime); 
        }else{//放大
        	mCamera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));   
        	//mCamera.translate(20.0f, 0.0f, (1300-1300.0f*interpolatedTime));
        }
        
        mCamera.rotateX(degrees);
        mCamera.getMatrix(matrix);   
        mCamera.restore();   
        matrix.preTranslate(-centerX, -centerY);   
        matrix.postTranslate(centerX, centerY);   
    }   
}  
