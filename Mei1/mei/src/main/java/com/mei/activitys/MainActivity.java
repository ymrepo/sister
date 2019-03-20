package com.mei.activitys;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mei.R;
import com.mei.adapter.ListAdapter;
import com.mei.adapter.MyPagerAdapter;
import com.mei.app.MApplication;
import com.mei.model.ListData;
import com.mei.model.item;
import com.mei.utils.Constant;
import com.mei.utils.DensityUtil;
import com.mei.widges.CreateDialog;
import com.mei.widges.MDialog;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

    public static Activity mActivity;
    private MDialog waitDialog;
    private ImageView mSetting;
    private ImageView mTitleImg;
    private PullToRefreshListView mPullToRefreshView0;
    private PullToRefreshListView mPullToRefreshView1;
    private PullToRefreshListView mPullToRefreshView2;
    private PullToRefreshListView mPullToRefreshView3;
    private ListAdapter mAdapter0;
    private ListAdapter mAdapter1;
    private ListAdapter mAdapter2;
    private ListAdapter mAdapter3;
    private List<item> mList0;
    private List<item> mList1;
    private List<item> mList2;
    private List<item> mList3;
    private List<View> listViews;
    private ViewPager mPager;
    private View mView0;
    private View mView1;
    private View mView2;
    private View mView3;
    private int bmpW;
    private ImageView cursor;
    private TextView t0, t1, t2, t3;
    private int currIndex = 0;
    private LocalBroadcastManager mLocalBroadcastManager;
    private MsgReceiver mRecv = new MsgReceiver();

    private void registerReceiver() {
        if (null == mRecv) {
            mRecv = new MsgReceiver();
        }
        IntentFilter intentFilter = new IntentFilter("MSG");
        mLocalBroadcastManager.registerReceiver(mRecv, intentFilter);
    }

    private void unregisterReceiver() {
        if (null != mRecv)
            mLocalBroadcastManager.unregisterReceiver(mRecv);
    }

    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            Log.e("test", "ok");
            if (intent.getAction().equals("MSG")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MApplication.sfv.start();
                        MApplication.sfv.seekTo(intent.getIntExtra("time", 0));
                    }
                }, 500);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerReceiver();

        mActivity = this;
        mSetting = (ImageView) findViewById(R.id.setting);
        mSetting.setVisibility(View.VISIBLE);
        mTitleImg = (ImageView) findViewById(R.id.titleimg);
        mTitleImg.setVisibility(View.VISIBLE);
        t0 = (TextView) findViewById(R.id.text0);
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
        t3 = (TextView) findViewById(R.id.text3);
        t0.setOnClickListener(new MyOnClickListener(0));
        t1.setOnClickListener(new MyOnClickListener(1));
        t2.setOnClickListener(new MyOnClickListener(2));
        t3.setOnClickListener(new MyOnClickListener(3));

        cursor = (ImageView) findViewById(R.id.cursor);
        cursor.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.getWidthPixels(this) / 4, DensityUtil.dip2px(this, 3)));
        bmpW = DensityUtil.getWidthPixels(this) / 4;
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        mView0 = mInflater.inflate(R.layout.item_viewpage, null);
        mView1 = mInflater.inflate(R.layout.item_viewpage, null);
        mView2 = mInflater.inflate(R.layout.item_viewpage, null);
        mView3 = mInflater.inflate(R.layout.item_viewpage, null);
        listViews.add(mView0);
        listViews.add(mView1);
        listViews.add(mView2);
        listViews.add(mView3);

        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mPullToRefreshView0 = (PullToRefreshListView) mView0.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView0.getRefreshableView().setSelector(android.R.color.transparent);
        mPullToRefreshView0.setMode(Mode.BOTH);
        mPullToRefreshView0.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onHeaderRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onFooterRefresh();
            }
        });

        mPullToRefreshView1 = (PullToRefreshListView) mView1.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView1.getRefreshableView().setSelector(android.R.color.transparent);
        mPullToRefreshView1.setMode(Mode.BOTH);
        mPullToRefreshView1.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onHeaderRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onFooterRefresh();
            }
        });

        mPullToRefreshView2 = (PullToRefreshListView) mView2.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView2.getRefreshableView().setSelector(android.R.color.transparent);
        mPullToRefreshView2.setMode(Mode.BOTH);
        mPullToRefreshView2.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onHeaderRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onFooterRefresh();
            }
        });

        mPullToRefreshView3 = (PullToRefreshListView) mView3.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView3.getRefreshableView().setSelector(android.R.color.transparent);
        mPullToRefreshView3.setMode(Mode.BOTH);
        mPullToRefreshView3.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onHeaderRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onFooterRefresh();
            }
        });

        mList0 = new ArrayList<item>();
        mList1 = new ArrayList<item>();
        mList2 = new ArrayList<item>();
        mList3 = new ArrayList<item>();
        invokeDetailData(null, System.currentTimeMillis() + "");
        invokeDetailData("text", System.currentTimeMillis() + "");
        invokeDetailData("picture", System.currentTimeMillis() + "");
        invokeDetailData("video", System.currentTimeMillis() + "");

        MApplication.height = DensityUtil.getHeightPixels(this);
        MApplication.width = DensityUtil.getWidthPixels(this);

        mSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent mIntent = new Intent(mActivity, SearchActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("onPageStart");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("onPageEnd");
        MobclickAgent.onPause(this);
    }

    private void invokeDetailData(final String type, String time) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("n", "20");
        params.addQueryStringParameter("lastTime", time);
        if (type == null) {
            waitDialog = CreateDialog.waitingDialog(mActivity, "");
        } else if (type.equals("text")) {
            params.addQueryStringParameter("type", "text");
        } else if (type.equals("picture")) {
            params.addQueryStringParameter("type", "picture");
        } else if (type.equals("video")) {
            params.addQueryStringParameter("type", "video");
        }

        MApplication.http.send(HttpRequest.HttpMethod.POST, Constant.test, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        CreateDialog.dismiss(mActivity, waitDialog);
                        Type t = new TypeToken<ListData>() {
                        }.getType();
                        ListData mListResult = new Gson().fromJson(responseInfo.result, t);

                        if (type == null) {
                            mList0 = mListResult.getJokeItems();
                            if (mList0 != null && mList0.size() > 0) {
                                mAdapter0 = new ListAdapter(mActivity, mList0);
                                mPullToRefreshView0.setAdapter(mAdapter0);
                            }

                        } else if (type.equals("text")) {
                            mList1 = mListResult.getJokeItems();
                            if (mList1 != null && mList1.size() > 0) {
                                mAdapter1 = new ListAdapter(mActivity, mList1);
                                mPullToRefreshView1.setAdapter(mAdapter1);
                            }

                        } else if (type.equals("picture")) {
                            mList2 = mListResult.getJokeItems();
                            if (mList2 != null && mList2.size() > 0) {
                                mAdapter2 = new ListAdapter(mActivity, mList2);
                                mPullToRefreshView2.setAdapter(mAdapter2);
                            }

                        } else if (type.equals("video")) {
                            mList3 = mListResult.getJokeItems();
                            if (mList3 != null && mList3.size() > 0) {
                                mAdapter3 = new ListAdapter(mActivity, mList3);
                                mPullToRefreshView3.setAdapter(mAdapter3);
                            }

                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CreateDialog.dismiss(mActivity, waitDialog);
                    }
                });

    }



    private void onHeaderRefresh() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("n", "20");
        if (currIndex == 1) {
            params.addQueryStringParameter("type", "text");
        } else if (currIndex == 2) {
            params.addQueryStringParameter("type", "picture");
        } else if (currIndex == 3) {
            params.addQueryStringParameter("type", "video");
        }
        params.addQueryStringParameter("lastTime", System.currentTimeMillis() + "");

        MApplication.http.send(HttpRequest.HttpMethod.POST, Constant.test, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        Log.e("MEI","responseInfo:"+responseInfo.result);
                        CreateDialog.dismiss(mActivity, waitDialog);
                        Type type = new TypeToken<ListData>() {
                        }.getType();
                        ListData mListResult = new Gson().fromJson(responseInfo.result, type);
                        if (currIndex == 0) {
                            mList0 = mListResult.getJokeItems();
                            if (mList0 != null && mList0.size() > 0) {
                                mAdapter0 = new ListAdapter(mActivity, mList0);
                                mPullToRefreshView0.setAdapter(mAdapter0);
                                mPullToRefreshView0.onRefreshComplete();
                            }
                        } else if (currIndex == 1) {
                            mList1 = mListResult.getJokeItems();
                            if (mList1 != null) {
                                mAdapter1 = new ListAdapter(mActivity, mList1);
                                mPullToRefreshView1.setAdapter(mAdapter1);
                                mPullToRefreshView1.onRefreshComplete();
                            }

                        } else if (currIndex == 2) {
                            mList2 = mListResult.getJokeItems();
                            if (mList2 != null && mList2.size() > 0) {
                                mAdapter2 = new ListAdapter(mActivity, mList2);
                                mPullToRefreshView2.setAdapter(mAdapter2);
                                mPullToRefreshView2.onRefreshComplete();
                            }

                        } else if (currIndex == 3) {
                            mList3 = mListResult.getJokeItems();
                            if (mList3 != null && mList3.size() > 0) {
                                mAdapter3 = new ListAdapter(mActivity, mList3);
                                mPullToRefreshView3.setAdapter(mAdapter3);
                                mPullToRefreshView3.onRefreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CreateDialog.dismiss(mActivity, waitDialog);
                        if (currIndex == 0) {
                            mPullToRefreshView0.onRefreshComplete();
                        } else if (currIndex == 1) {
                            mPullToRefreshView1.onRefreshComplete();
                        } else if (currIndex == 2) {
                            mPullToRefreshView2.onRefreshComplete();
                        } else if (currIndex == 3) {
                            mPullToRefreshView3.onRefreshComplete();
                        }
                    }
                });
    }

    private void onFooterRefresh() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("n", "20");
        if (currIndex == 0 && mList0 != null && mList0.size() > 0) {
            params.addQueryStringParameter("lastTime", mList0.get(mList0.size() - 1).getCtime() + "");
        } else if (currIndex == 1 && mList1 != null && mList1.size() > 0) {
            params.addQueryStringParameter("type", "text");
            params.addQueryStringParameter("lastTime", mList1.get(mList1.size() - 1).getCtime() + "");
        } else if (currIndex == 2 && mList2 != null && mList2.size() > 0) {
            params.addQueryStringParameter("type", "picture");
            params.addQueryStringParameter("lastTime", mList2.get(mList2.size() - 1).getCtime() + "");
        } else if (currIndex == 3 && mList3 != null && mList3.size() > 0) {
            params.addQueryStringParameter("type", "video");
            params.addQueryStringParameter("lastTime", mList3.get(mList3.size() - 1).getCtime() + "");
        }

        MApplication.http.send(HttpRequest.HttpMethod.POST, Constant.test, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        CreateDialog.dismiss(mActivity, waitDialog);
                        Type type = new TypeToken<ListData>() {
                        }.getType();
                        ListData mListResult = new Gson().fromJson(responseInfo.result, type);
                        if (mListResult != null && mListResult.getJokeItems() != null && mListResult.getJokeItems().size() > 0) {
                            if (currIndex == 0) {
                                mList0.addAll(mListResult.getJokeItems());
                                mAdapter0.notifyDataSetChanged();
                                mPullToRefreshView0.onRefreshComplete();
                            } else if (currIndex == 1) {
                                mList1.addAll(mListResult.getJokeItems());
                                mAdapter1.notifyDataSetChanged();
                                mPullToRefreshView1.onRefreshComplete();
                            } else if (currIndex == 2) {
                                mList2.addAll(mListResult.getJokeItems());
                                mAdapter2.notifyDataSetChanged();
                                mPullToRefreshView2.onRefreshComplete();
                            } else if (currIndex == 3) {
                                mList3.addAll(mListResult.getJokeItems());
                                mAdapter3.notifyDataSetChanged();
                                mPullToRefreshView3.onRefreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        CreateDialog.dismiss(mActivity, waitDialog);
                        if (currIndex == 0) {
                            mPullToRefreshView0.onRefreshComplete();
                        } else if (currIndex == 1) {
                            mPullToRefreshView1.onRefreshComplete();
                        } else if (currIndex == 2) {
                            mPullToRefreshView2.onRefreshComplete();
                        } else if (currIndex == 3) {
                            mPullToRefreshView3.onRefreshComplete();
                        }
                    }
                });
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    ;

    public class MyOnPageChangeListener implements OnPageChangeListener {
        int one = bmpW;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            t0.setTextColor(getResources().getColor(R.color.text_grey10));
            t1.setTextColor(getResources().getColor(R.color.text_grey10));
            t2.setTextColor(getResources().getColor(R.color.text_grey10));
            t3.setTextColor(getResources().getColor(R.color.text_grey10));
            switch (arg0) {
                case 0:
                    t0.setTextColor(getResources().getColor(R.color.fragment_title));
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(one * 2, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(one * 3, 0, 0, 0);
                    }
                    break;
                case 1:
                    t1.setTextColor(getResources().getColor(R.color.fragment_title));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(one * 2, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(one * 3, one, 0, 0);
                    }
                    break;
                case 2:
                    t2.setTextColor(getResources().getColor(R.color.fragment_title));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one * 2, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, one * 2, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(one * 3, one * 2, 0, 0);
                    }
                    break;
                case 3:
                    t3.setTextColor(getResources().getColor(R.color.fragment_title));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(0, one * 3, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, one * 3, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(one * 2, one * 3, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(200);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
