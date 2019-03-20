package com.mei.widges;

import android.app.Activity;
import android.view.View;

import com.mei.R;

public class CreateDialog {
	static MDialog mAlertDialog;

	static public MDialog waitingDialog(final Activity activity, final String message) {
		if (activity == null || activity.isFinishing()) {
			return null;
		}
		final MDialog waitDialog = new MDialog(activity, R.style.dialog, message);
		waitDialog.setCanceledOnTouchOutside(false);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!waitDialog.isShowing())
					waitDialog.show();
			}
		});
		
		return waitDialog;
	}

	static public MDialog alertDialog(Activity activity, String title, String hint, String bottonL, String bottonR,
			View.OnClickListener cancelBtnClickListener, View.OnClickListener confirmBtnClickListener) {
		if (activity == null || activity.isFinishing()) {
			return null;
		}

		MDialog alertDialog = new MDialog(activity, R.style.dialog, title, hint, bottonL, bottonR, cancelBtnClickListener,
				confirmBtnClickListener);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
		alertDialog.setCancelable(true);

		return alertDialog;
	}

	static public MDialog updateDialog(Activity activity, String title, String hint, View.OnClickListener cancelBtnClickListener,
			View.OnClickListener confirmBtnClickListener, String updateHint) {
		if (activity == null || activity.isFinishing()) {
			return null;
		}

		MDialog alertDialog = new MDialog(activity, R.style.dialog, title, hint, cancelBtnClickListener, confirmBtnClickListener, updateHint);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		alertDialog.setCancelable(false);
		return alertDialog;
	}

	static public MDialog agreementDialog(Activity activity, String hint, String bottonL, String bottonR,
			View.OnClickListener cancelBtnClickListener, View.OnClickListener confirmBtnClickListener) {
		if (activity == null || activity.isFinishing()) {
			return null;
		}

		MDialog alertDialog = new MDialog(activity, R.style.dialog, hint, bottonL, bottonR, cancelBtnClickListener, confirmBtnClickListener);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		alertDialog.setCancelable(true);

		return alertDialog;
	}
	
	public static void dismiss(Activity activity, final MDialog waitDialog) {
		if (activity != null && !activity.isFinishing() && waitDialog != null && waitDialog.isShowing()) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					waitDialog.dismiss();
				}
			});
		}
	}
}
