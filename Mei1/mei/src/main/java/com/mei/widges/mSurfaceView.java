package com.mei.widges;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.mei.activitys.MainActivity;
import com.mei.activitys.RawplayActivity;
import com.mei.app.MApplication;

public class mSurfaceView extends SurfaceView implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, mMediaController.MediaPlayerControl, OnVideoSizeChangedListener, OnCompletionListener{

	private SurfaceHolder sfh;
	private Context ct;
	private MediaPlayer player = null; 
	private mMediaController controller = null; 
	private int w;
	private int h;
	private onCompleted onc;
	private boolean isFullScreen = false;
	private String mUri;
	
    public interface onCompleted {
        void loadComplete();
        void playComplete();
    }
	
	public mSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ct = context;
		setKeepScreenOn(true);  
        setFocusable(true);  
        sfh = getHolder();  
        sfh.addCallback(this);
        
        player = new MediaPlayer(); 
		controller = new mMediaController(ct); 
		
		player.setAudioStreamType(AudioManager.STREAM_MUSIC); 
		player.setOnPreparedListener(this); 
		player.setOnVideoSizeChangedListener(this);
		player.setOnCompletionListener(this);
		
	}
	
    public void setCompleteListener(onCompleted player) {
        onc = player;
    }
	
	public void setUri(String url) {
		mUri = url;
		player.reset();
		try {
			player.setDataSource(ct, Uri.parse(url));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		player.prepareAsync();
	}
	
	public void setSize(int width, int height){
		w = width;
		h = height;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(controller.isShown())
			controller.hide();
		else
			controller.show(); 
		return false;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int width, int height) {
//		Log.e("surfaceChanged", width+"  "+height);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		player.setDisplay(holder); 
//		Log.e("surfaceCreated", "...");
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.e("surfaceDestroyed", "...");
		if (player != null) {
	    	player.pause();
	    }
	}

	@Override
	public void start() {
		player.start();
	}


	@Override
	public void pause() {
		player.pause();
	}


	@Override
	public int getDuration() {
		return player.getDuration();
	}


	@Override
	public int getCurrentPosition() {
		return player.getCurrentPosition();
	}


	@Override
	public void seekTo(int pos) {
		player.seekTo(pos);
	}


	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}


	@Override
	public int getBufferPercentage() {
		return 0;
	}


	@Override
	public boolean canPause() {
		return true;
	}


	@Override
	public boolean canSeekBackward() {
		return true;
	}


	@Override
	public boolean canSeekForward() {
		return true;
	}


	@Override
	public boolean isFullScreen() {
		return isFullScreen ;
	}

	public void setFullScreen(boolean sf){
		isFullScreen = sf;
	}

	@Override
	public void toggleFullScreen() {
		if(!isFullScreen){
			player.pause();
			MApplication.sfv = this;
			Intent m = new Intent(ct, RawplayActivity.class);
			m.putExtra("uri", mUri);
			m.putExtra("time", player.getCurrentPosition());
//			ct.startActivity(m);
			MainActivity.mActivity.startActivityForResult(m, Activity.RESULT_OK);
		}else{
			((Activity)ct).finish();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		controller.setMediaPlayer(this); 
		controller.setAnchorView((FrameLayout)getParent()); 
		player.start();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//		Log.e("onVideoSizeChanged", width+"  "+height+"    "+w+"  "+h);
		if(width != 0 && height != 0){
			if((float)width / (float)height > (float)w / (float)h)
				sfh.setFixedSize(w, (int)(height / (float)width * w));
			else
				sfh.setFixedSize((int)(width / (float)height * h), h);
			if(onc != null)
				onc.loadComplete();
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		if(onc != null)
			onc.playComplete();
	}
	
}
