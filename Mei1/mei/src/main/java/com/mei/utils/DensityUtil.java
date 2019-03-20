package com.mei.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

public class DensityUtil {
	static int h;
	static int w;
	
	public static int getWidthPixels(Context ct){
		return ct.getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int getHeightPixels(Context ct){
		return ct.getResources().getDisplayMetrics().heightPixels;
	}
	
	public static int getRealHeightPixels(Activity ct){
		Rect frame = new Rect();
		ct.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.bottom - frame.top;
	}
	
	public static int getTopHeightPixels(Activity ct){
		Rect frame = new Rect();
		ct.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}
	
	 /** 
     * 根据手机的分辨率dp转成px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
    	h = context.getResources().getDisplayMetrics().heightPixels;
    	w = context.getResources().getDisplayMetrics().widthPixels;
        float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率px(像素)转成dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
    	h = context.getResources().getDisplayMetrics().heightPixels;
    	w = context.getResources().getDisplayMetrics().widthPixels;
        float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    /** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    } 
}
