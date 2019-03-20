package com.mei.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.mei.R;
import com.mei.utils.DensityUtil;
import com.mei.utils.PreferenceHelper;
public class WelcomeActivity extends Activity {
	private RelativeLayout mRelativeLayout;
	private ViewPager viewPager; // android-support-v4涓殑婊戝姩缁勪欢
	private List<View> imageViewsRL; // 婊戝姩鐨勫浘鐗囬泦锟�?
	private int screenHeight;
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRelativeLayout = new RelativeLayout(this);
		mRelativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		setContentView(mRelativeLayout);

		initViews();
	}
	
	private void initViews() {
		screenHeight = DensityUtil.getRealHeightPixels(this);
		screenWidth = DensityUtil.getWidthPixels(this);
		imageViewsRL = new ArrayList<View>();
		
		//鎸夐挳甯冨眬
		RelativeLayout.LayoutParams mBtLayoutParams = new RelativeLayout.LayoutParams(screenWidth/2, screenWidth/8);
		mBtLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBtLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		mBtLayoutParams.bottomMargin = DensityUtil.getHeightPixels(this) / 3;
		
		ImageView mImg0 = new ImageView(this);
		mImg0.setBackgroundResource(R.drawable.sp0);
		mImg0.setScaleType(ScaleType.CENTER_CROP);
		imageViewsRL.add(mImg0);
		
		ImageView mImg1 = new ImageView(this);
		mImg1.setBackgroundResource(R.drawable.sp1);
		mImg1.setScaleType(ScaleType.CENTER_CROP);
		imageViewsRL.add(mImg1);
		
		
		RelativeLayout ThirdRL = new RelativeLayout(this);
		
		ImageView mImg2 = new ImageView(this);
		mImg2.setBackgroundResource(R.drawable.sp2);
		mImg2.setScaleType(ScaleType.CENTER_CROP);
		ThirdRL.addView(mImg2);
		
		ImageView mStartBt = new ImageView(this);
		mStartBt.setLayoutParams(mBtLayoutParams);
		ThirdRL.addView(mStartBt);
		
		mStartBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PreferenceHelper.setFirstInApp(false);
				Intent mIntent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		
		imageViewsRL.add(ThirdRL);
		
		viewPager = new ViewPager(this);
		viewPager.setAdapter(new MyAdapter());
		mRelativeLayout.addView(viewPager);
	}

	/**
	 * 濉厖ViewPager椤甸潰鐨勶拷?锟介厤锟�?
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imageViewsRL.size();
		}
		@Override
		public Object instantiateItem(View arg0, final int arg1) {
			((ViewPager) arg0).addView(imageViewsRL.get(arg1),0);
			return imageViewsRL.get(arg1);
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}
}
