package com.mei.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mei.R;
import com.mei.activitys.DetailActivity;
import com.mei.app.MApplication;
import com.mei.model.item;
import com.mei.utils.Constant;
import com.mei.utils.DensityUtil;
import com.mei.widges.MDialog;
import com.mei.widges.MyToast;
import com.mei.widges.mMediaController;
import com.mei.widges.mSurfaceView;
import com.mei.widges.mSurfaceView.onCompleted;
import com.umeng.analytics.MobclickAgent;
import java.util.List;

public class ListAdapter extends BaseAdapter {
	private Activity context;
	private List<item> mList;
	private MDialog waitDialog;
	private mMediaController mMc;

	public ListAdapter(Activity context, List<item> mList) {
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.share = (ImageView) convertView.findViewById(R.id.share);
			holder.item_text = (TextView) convertView.findViewById(R.id.item_text);
			holder.item_img = (SimpleDraweeView) convertView.findViewById(R.id.item_img);
			holder.item_gif = (SimpleDraweeView) convertView.findViewById(R.id.item_gif);
//			holder.item_bar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar);
			holder.item_pb = (ProgressBar) convertView.findViewById(R.id.item_pb);
			holder.item_play = (ImageView) convertView.findViewById(R.id.item_play);
			holder.item_video = (mSurfaceView) convertView.findViewById(R.id.item_video);
			holder.item_video_rl = (FrameLayout) convertView.findViewById(R.id.item_video_rl);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
			holder.share.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
				}
			});
			
			holder.item_text.setText(mList.get(position).getText());
			holder.item_gif.setVisibility(View.GONE);
			holder.item_img.setVisibility(View.GONE);
			holder.item_play.setVisibility(View.GONE);
			holder.item_pb.setVisibility(View.GONE);
			holder.item_video_rl.setVisibility(View.GONE);
			holder.item_video.pause();
			
			if("picture".equals(mList.get(position).getType())){
				holder.item_img.setVisibility(View.VISIBLE);
				holder.item_img.setOnClickListener(new PicOnClickListener(position));
				
				ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
					public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
						RelativeLayout.LayoutParams mRL = (LayoutParams) holder.item_img.getLayoutParams();
						if(imageInfo.getHeight() > imageInfo.getWidth()){
							mRL.width = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * imageInfo.getWidth() / imageInfo.getHeight();
							mRL.height = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
						}else{
							mRL.width = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
							mRL.height = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * imageInfo.getHeight() / imageInfo.getWidth();
						}
						holder.item_img.setLayoutParams(mRL);
					};
				};
				DraweeController  draweeController1 = Fresco.newDraweeControllerBuilder()
						.setUri(Uri.parse(mList.get(position).getPoster()))
						.setControllerListener(controllerListener)
						.build();
				holder.item_img.setController(draweeController1);
				
			} else if("gif".equals(mList.get(position).getType())){
				holder.item_img.setVisibility(View.VISIBLE);
				holder.item_play.setVisibility(View.VISIBLE);
				holder.item_play.setImageResource(R.drawable.play);
				holder.item_img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						MobclickAgent.onEvent(context, "program_name",  mList.get(position).getText());
						holder.item_gif.setVisibility(View.VISIBLE);
						holder.item_play.setVisibility(View.GONE);
						holder.item_pb.setVisibility(View.VISIBLE);
						holder.item_img.setImageURI(Uri.parse(mList.get(position).getPicture()));

						ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
							@Override
							public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
								holder.item_gif.setOnClickListener(new GifOnClickListener(position, holder,null));
								holder.item_img.setVisibility(View.GONE);
								holder.item_pb.setVisibility(View.GONE);
							};
						};
						DraweeController  draweeController1 = Fresco.newDraweeControllerBuilder()
								.setAutoPlayAnimations(true)
								.setUri(Uri.parse(mList.get(position).getPicture()))
								.setControllerListener(controllerListener)
								.build();
						holder.item_gif.setController(draweeController1);
					}
				});
				
				ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
					public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
						RelativeLayout.LayoutParams mRL = (LayoutParams) holder.item_img.getLayoutParams();
						if(imageInfo.getHeight() > imageInfo.getWidth()){
							mRL.width = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * imageInfo.getWidth() / imageInfo.getHeight();
							mRL.height = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
						}else{
							mRL.width = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
							mRL.height = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * imageInfo.getHeight() / imageInfo.getWidth();
						}
						holder.item_img.setLayoutParams(mRL);
						holder.item_gif.setLayoutParams(mRL);
					};
				};
				DraweeController  draweeController1 = Fresco.newDraweeControllerBuilder()
						.setUri(Uri.parse(mList.get(position).getPoster()))
						.setControllerListener(controllerListener)
						.build();
				holder.item_img.setController(draweeController1);
				
			} else if("video".equals(mList.get(position).getType())){
				holder.item_img.setVisibility(View.VISIBLE);
				holder.item_play.setVisibility(View.VISIBLE);
				holder.item_play.setImageResource(R.drawable.play_video_normal);
				holder.item_video_rl.setVisibility(View.VISIBLE);
				
				ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
					public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
						RelativeLayout.LayoutParams mRL = (LayoutParams) holder.item_img.getLayoutParams();
						int w;
						int h;
						if(imageInfo.getHeight() > imageInfo.getWidth()){
							mRL.width = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * imageInfo.getWidth() / imageInfo.getHeight();
							mRL.height = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
						}else{
							mRL.width = DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30);
							mRL.height = (DensityUtil.getWidthPixels(context) - DensityUtil.dip2px(context, 30)) * 9 / 16;
						}
						w = mRL.width;
						h = mRL.height;
						holder.item_img.setLayoutParams(mRL);
						holder.item_video_rl.setLayoutParams(mRL);
						holder.item_img.setOnClickListener(new VideoImgOnClickListener(position, holder, w, h));
					};
				};
				DraweeController  draweeController1 = Fresco.newDraweeControllerBuilder()
						.setUri(Uri.parse(mList.get(position).getPoster()))
						.setControllerListener(controllerListener)
						.build();
				holder.item_img.setController(draweeController1);
			}
		} catch (NotFoundException e) {
			Log.e("NotFoundException", e.getMessage());
		} 
		
		return convertView;
	}
	
	private class PicOnClickListener implements OnClickListener{
		private int position;
		public PicOnClickListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View arg0) {
			MobclickAgent.onEvent(context, "program_name",  mList.get(position).getText());
			Intent mIntent = new Intent(context, DetailActivity.class);
			mIntent.putExtra(Constant.TYPE, 0);
			mIntent.putExtra(Constant.URL, mList.get(position).getPicture());
			context.startActivity(mIntent);
			context.overridePendingTransition(R.anim.enter_alpha, R.anim.out_alpha);
		}
	}
	
	private class GifOnClickListener implements OnClickListener{
		private int position;
		private ViewHolder mHolder;
		private Animatable animatable;
		public GifOnClickListener(int pos, ViewHolder holder, Animatable anim) {
			position = pos;
			mHolder = holder;
			animatable = anim;
		}

		@Override
		public void onClick(View arg0) {
			if(animatable!=null &&animatable.isRunning()){
				animatable.stop();
				mHolder.item_play.setVisibility(View.VISIBLE);
			}else{
				if(animatable!=null)animatable.start();
				mHolder.item_play.setVisibility(View.GONE);
			}
		}
	}
	
	private class VideoImgOnClickListener implements OnClickListener{
		private int position;
		private ViewHolder mHolder;
		private int w;
		private int h;
		
		public VideoImgOnClickListener(int pos, ViewHolder holder, int width, int height) {
			position = pos;
			mHolder = holder;
			w = width;
			h = height;
		}
		@Override
		public void onClick(View arg0) {
			MobclickAgent.onEvent(context, "program_name",  mList.get(position).getText());
			mHolder.item_play.setVisibility(View.GONE);
			mHolder.item_pb.setVisibility(View.VISIBLE);
			mHolder.item_video.setCompleteListener(new onCompleted() {
				@Override
				public void loadComplete() {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							mHolder.item_img.setVisibility(View.GONE);
							mHolder.item_pb.setVisibility(View.GONE);
						}
					}, 1000);					
				}
				@Override
				public void playComplete() {
				}
			});
			invokeURL(mList.get(position).getVideoid(), mList.get(position).getText(), mHolder, w, h);
		}
	}
	
	private void invokeURL(String baseURL, final String title, final ViewHolder mHolder, final int w, final int h) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("url", baseURL);
		MApplication.http.send(HttpRequest.HttpMethod.POST, Constant.playurl, params,
			new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					if(!TextUtils.isEmpty(responseInfo.result)){
						mHolder.item_video.setSize(w, h);
						mHolder.item_video.setUri(responseInfo.result);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					new MyToast(context).showinfo("url failed~");
				}
			});
	}
	
	static class ViewHolder {
		TextView item_text;
		SimpleDraweeView item_img;
		ImageView item_play;
		SimpleDraweeView item_gif;
		mSurfaceView item_video;
		FrameLayout item_video_rl;
		ProgressBar item_pb;
		ImageView share;
//		RoundProgressBar item_bar;
	}
}