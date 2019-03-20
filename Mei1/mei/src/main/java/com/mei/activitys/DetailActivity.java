package com.mei.activitys;

//import me.relex.photodraweeview.OnViewTapListener;
//import me.relex.photodraweeview.PhotoDraweeView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mei.R;
import com.mei.app.MApplication;
import com.mei.utils.Constant;
import com.umeng.analytics.MobclickAgent;

//import android.graphics.drawable.Animatable;
//import android.net.Uri;
//import android.os.Handler;
//import android.text.TextUtils;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
//import com.facebook.drawee.controller.BaseControllerListener;
//import com.facebook.imagepipeline.image.ImageInfo;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//import com.sister.app.MApplication;
//import com.sister.utils.Constant;
//import com.umeng.analytics.MobclickAgent;

public class DetailActivity extends Activity {
	private int type = 0;
	private String url;
	private SimpleDraweeView mImageView;
	private float scale = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic);

		mImageView = (SimpleDraweeView) findViewById(R.id.pic);
		//mImageView.setMaximumScale(10);
		//mImageView.setOnViewTapListener(new OnViewTapListener() {
		//	@Override
		//	public void onViewTap(View view, float x, float y) {
		//		finish();
		//	}
		//});

		Intent mIntent = getIntent();
		url = mIntent.getStringExtra(Constant.URL);

		if (!TextUtils.isEmpty(url)) {
			ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();

			PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
			controller.setOldController(mImageView.getController());
			controller.setImageRequest(request);
			controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
				@Override
				public void onFinalImageSet(String id, final ImageInfo imageInfo, Animatable animatable) {
					super.onFinalImageSet(id, imageInfo, animatable);
					if (imageInfo == null || mImageView == null) {
						return;
					}
					scale = (float) MApplication.width / (float)imageInfo.getWidth();
					if(scale * imageInfo.getHeight() <= MApplication.height)
						scale = 1;
					else
						scale = (float)MApplication.width / ((float)imageInfo.getWidth() / (float)imageInfo.getHeight() * (float)MApplication.height);

					//mImageView.update(imageInfo.getWidth(), (int)((float)imageInfo.getHeight() / (float)imageInfo.getWidth() * (float)MApplication.width));
					//new Handler().postDelayed(new Runnable() {
					//	@Override
					//	public void run() {
					//		mImageView.setScale(scale, 0, 0, false);
					//	}
					//}, 10);
				}
			});
			mImageView.setController(controller.build());
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.out_alpha);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("onPageStart");
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("onPageEnd");
		MobclickAgent.onPause(this);
	}
}
