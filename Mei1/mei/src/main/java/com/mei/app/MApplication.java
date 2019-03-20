package com.mei.app;

import android.app.Application;
import android.content.Context;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lidroid.xutils.HttpUtils;
import com.mei.widges.mSurfaceView;

public class MApplication extends Application {

	public static HttpUtils http;
	public static Context mContext;
	public static boolean isCheck = false;
	public static int height;
	public static int width;
	public static mSurfaceView sfv;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = this;
		http = new HttpUtils();
		
		ImagePipelineConfig config0 = ImagePipelineConfig.newBuilder(this).build();
		Fresco.initialize(this,config0);
	}

}
