package com.mei.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ProgressBar;

import com.mei.R;
import com.mei.app.MApplication;
import com.mei.utils.DensityUtil;
import com.mei.widges.mSurfaceView;
import com.mei.widges.mSurfaceView.onCompleted;

public class RawplayActivity extends Activity {

	private mSurfaceView sv;
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rawplay);
		
		sv = (mSurfaceView) findViewById(R.id.video);
		pb = (ProgressBar) findViewById(R.id.pb);
		
		sv.setFullScreen(true);
		sv.setSize(DensityUtil.getWidthPixels(this), DensityUtil.getHeightPixels(this));
		sv.setUri(getIntent().getStringExtra("uri"));
		sv.setCompleteListener(new onCompleted() {
			@Override
			public void loadComplete() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						sv.seekTo(getIntent().getIntExtra("time", 0));
						pb.setVisibility(View.GONE);
					}
				}, 1000);
			}

			@Override
			public void playComplete() {
				finish();
			}
		});
	}
	
	@Override
	public void finish() {
		sendTime();
		super.finish();
	}
	
	private void sendTime(){
		Intent it1 = new Intent();
	    it1.setAction("MSG");
	    it1.putExtra("time", sv.getCurrentPosition());
		LocalBroadcastManager.getInstance(MApplication.mContext).sendBroadcast(it1);
	}
}
