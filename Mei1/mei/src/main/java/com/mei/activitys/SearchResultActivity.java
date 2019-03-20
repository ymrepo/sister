package com.mei.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mei.R;
import com.mei.utils.PreferenceHelper;

public class SearchResultActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activigy);
        webView = (WebView) findViewById(R.id.webView);
        findViewById(R.id.back).setVisibility(View.VISIBLE);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initSetting();

        if (getIntent() != null) {
            String keyWord = getIntent().getStringExtra("keyword");
            if ("".equals(keyWord) || null == keyWord) return;

            String url = PreferenceHelper.getSearchUrl();
            url = url.replace("###", keyWord);
//                String url = "https://wap.sogou.com/web/sl?keyword=" + keyWord + "&bid=sogou-appi-0db32de7aed05af0";
            Log.e("MEI", " searchUrl:" + url);
            webView.loadUrl(url);

        }
    }

    private void initSetting() {
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
    }
}
