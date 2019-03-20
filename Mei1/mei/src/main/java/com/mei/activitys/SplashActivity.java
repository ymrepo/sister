package com.mei.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mei.R;
import com.mei.app.MApplication;
import com.mei.utils.Constant;
import com.mei.utils.PreferenceHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends Activity {

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash);
        MobclickAgent.setDebugMode(true);
        requestConfig();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//				if(PreferenceHelper.getFirstInApp()){
//		        	mIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
//		        }else{
                mIntent = new Intent(SplashActivity.this, MainActivity.class);
//		        }
                startActivity(mIntent);
                finish();
            }
        }, 2000);
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

    private void requestConfig() {
        MApplication.http.send(HttpRequest.HttpMethod.GET, Constant.config, null,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("MEI", responseInfo.result);
                        try {
                            if (responseInfo != null && null != responseInfo.result && !"".equals(responseInfo.result)) {
                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                String url = (String) jsonObject.get("searchUrl");
                                if (null != url && !"".equals(url)) {
                                    PreferenceHelper.setSearchUrl(url);
                                    Log.e("MEI", url);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }
}
