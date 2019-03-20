package com.mei.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.R;

public class AboutActivity extends Activity implements OnClickListener {
	private ImageView mBack;
	private TextView mTitle;
	private TextView mVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText("About");
		mBack = (ImageView) findViewById(R.id.back);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mVersion = (TextView) findViewById(R.id.version);
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			mVersion.setText("Version"+info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent;
		switch(v.getId()){
		case R.id.back:
			finish();
			break;
		}
	}
}
