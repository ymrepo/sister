package com.mei.widges;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mei.R;

public class MDialog extends Dialog {
	private String mTitle = null;
	private String mHintStr = null;
	private String mBottonL = null;
	private String mBottonR = null;
	private TextView mTileTextView;
	private TextView mHintTextView;
	private TextView mUpdateHintTextView;
	private View.OnClickListener mCancleClickListener = null;
	private View.OnClickListener mConfirmClickListener = null;
	private Button mCancelBtn = null;
	private Button mConfirmBtn = null;
	private ImageView mSplitIV = null;
	private String mUpdateHint = null;

	public void setDialogTextView(String str) {
		if (mHintTextView != null) {
			mHintTextView.setText(str);
		}
	}


	public MDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public MDialog(Context context, int theme, String hint) {
		super(context, theme);
		mHintStr = hint;
	}

	public MDialog(Context context, int theme, String title, String hint, String BottonL, String BottonR,
			View.OnClickListener cancelBtnClickListener, View.OnClickListener confirmBtnClickListener) {
		super(context, theme);
		mTitle = title;
		mHintStr = hint;
		mBottonL = BottonL;
		mBottonR = BottonR;
		mCancleClickListener = cancelBtnClickListener;
		mConfirmClickListener = confirmBtnClickListener;
	}

	public MDialog(Context context, int theme, String title, String hint, String BottonR, View.OnClickListener confirmBtnClickListener) {
		super(context, theme);
		mTitle = title;
		mHintStr = hint;
		mBottonR = BottonR;
		mConfirmClickListener = confirmBtnClickListener;
	}

	public MDialog(Context context, int theme, String hint, String BottonL, String BottonR, View.OnClickListener cancelBtnClickListener,
			View.OnClickListener confirmBtnClickListener) {
		super(context, theme);
		mHintStr = hint;
		mBottonL = BottonL;
		mBottonR = BottonR;
		mCancleClickListener = cancelBtnClickListener;
		mConfirmClickListener = confirmBtnClickListener;
	}

	public MDialog(Context context, int theme, View.OnClickListener cancelBtnClickListener, View.OnClickListener confirmBtnClickListener) {
		super(context, theme);
		mCancleClickListener = cancelBtnClickListener;
		mConfirmClickListener = confirmBtnClickListener;
	}

	public MDialog(Context context, int theme, String title, String hint, View.OnClickListener cancelBtnClickListener,
			View.OnClickListener confirmBtnClickListener, String updateHint) {
		super(context, theme);
		mTitle = title;
		mHintStr = hint;
		mCancleClickListener = cancelBtnClickListener;
		mConfirmClickListener = confirmBtnClickListener;
		mUpdateHint = updateHint;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mTitle != null) {
			setContentView(R.layout.dialog_alert);
			mTileTextView = (TextView) findViewById(R.id.alert_dialog_title);
			mHintTextView = (TextView) findViewById(R.id.alert_dialog_message);
			mConfirmBtn = (Button) findViewById(R.id.alert_dialog_confirm_btn);
			mConfirmBtn.setOnClickListener(mConfirmClickListener);
			mCancelBtn = (Button) findViewById(R.id.alert_dialog_cancel_btn);

			if (!TextUtils.isEmpty(mTitle))
				mTileTextView.setText(mTitle);
			else
				mTileTextView.setVisibility(View.GONE);

			if (!TextUtils.isEmpty(mHintStr))
				mHintTextView.setText(mHintStr);
			else
				mHintTextView.setVisibility(View.GONE);

			if (!TextUtils.isEmpty(mUpdateHint)) {
				mUpdateHintTextView = (TextView) findViewById(R.id.alert_dialog_updateHint);
				mUpdateHintTextView.setVisibility(View.VISIBLE);
				mUpdateHintTextView.setText(mUpdateHint);
			}

			if (!TextUtils.isEmpty(mBottonL))
				mCancelBtn.setText(mBottonL);
			if (!TextUtils.isEmpty(mBottonR))
				mConfirmBtn.setText(mBottonR);
			if (mCancleClickListener != null) {
				mCancelBtn.setOnClickListener(mCancleClickListener);
			} else {
				mSplitIV = (ImageView) findViewById(R.id.alert_dialog_middle);
				mSplitIV.setVisibility(View.GONE);
				mCancelBtn.setVisibility(View.GONE);
			}

		} else {
			setContentView(R.layout.dialog_waiting);
		}
	}

	public TextView getmTextView() {
		return mHintTextView;
	}

}
