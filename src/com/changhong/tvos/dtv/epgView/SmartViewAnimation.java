package com.changhong.tvos.dtv.epgView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SmartViewAnimation extends Animation {   
	private Camera mCamera;      
    private float mFromDegree;   //开始角度
    private float mToDegree;   //结束角度
    private float mTranslateX;   //位移X
    private float mTranslateY;    //位移Y
    private float mDepthZ;
    
    public SmartViewAnimation(float fromDegree, float toDegree,float tx, float ty,float depth) {   
        this.mFromDegree = fromDegree;   
        this.mToDegree = toDegree;   
        this.mTranslateX = tx;   
        this.mTranslateY = ty; 
        this.mDepthZ =depth;
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
        
        mCamera.save();   
        mCamera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));   
        mCamera.translate(0, 0, centerX);            
        mCamera.translate(0, 0, -centerX);   
        mCamera.rotateX(degrees);
        mCamera.getMatrix(matrix);   
        mCamera.restore();   
        matrix.preTranslate(-centerX, -centerY);   
        matrix.postTranslate(centerX, centerY);   
    }   
}  
