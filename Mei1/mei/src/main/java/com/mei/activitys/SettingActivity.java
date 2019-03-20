package com.mei.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mei.R;

public class SettingActivity extends Activity implements OnClickListener {

	private ImageView mBack;
	private TextView mTitle;
	private RelativeLayout mFeedback;
	private RelativeLayout mAbout;
	private RelativeLayout mSayGood;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText("Setting");
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mFeedback = (RelativeLayout) findViewById(R.id.feedback);
		mFeedback.setOnClickListener(this);
		mAbout = (RelativeLayout) findViewById(R.id.about);
		mAbout.setOnClickListener(this);
		mSayGood = (RelativeLayout) findViewById(R.id.saygood);
		mSayGood.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent;
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		case R.id.feedback:
			mIntent = new Intent(this, FeedbackActivity.class);
			startActivity(mIntent);
			break;
		case R.id.about:
			mIntent = new Intent(this, AboutActivity.class);
			startActivity(mIntent);
			break;
		case R.id.saygood:
			break;
		}
	}
}
