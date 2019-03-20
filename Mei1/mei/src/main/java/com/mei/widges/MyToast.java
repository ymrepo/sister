package com.mei.widges;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mei.R;
import com.mei.utils.DensityUtil;

public class MyToast extends Toast{
	private Activity ct;
	private LayoutInflater inflater;
	private LinearLayout.LayoutParams lp;
	private View layout;
	private TextView tv;
	private int textSize;
	private int screenWidth;

	public MyToast(Activity context) {
		super(context);
		this.ct = context;
		inflater = LayoutInflater.from(ct);
	}
	
	public void showinfo(String s){
		layout = inflater.inflate(R.layout.toast_bg, null);
		textSize = DensityUtil.sp2px(ct, 18);
		screenWidth = DensityUtil.getWidthPixels(ct);
		
		if(textSize*s.length() < screenWidth*3/4)
			lp = new LinearLayout.LayoutParams(textSize*s.length()+20, textSize*2); 
		else if(textSize*s.length() < screenWidth*3/2){
			lp = new LinearLayout.LayoutParams(screenWidth*3/4+20, textSize*3); 
		}else{
			lp = new LinearLayout.LayoutParams(screenWidth*3/4+20, textSize*4); 
		}
		
		tv = (TextView) layout.findViewById(R.id.pop_bg_text);
		tv.setText(s);
		tv.setLayoutParams(lp);
		setGravity(Gravity.CENTER, 0, -DensityUtil.dip2px(ct, 30));
		setDuration(LENGTH_SHORT);
		setView(layout);
		show();
	}
}
