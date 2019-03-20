package com.mei.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;

public class CommonUtils {
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String a(String s) {
		if (TextUtils.isEmpty(s)) {
			return null;
		}
		byte[] _s = s.getBytes();
		int strLen = _s.length;
		int i = 0;
		for (i = 0; i < strLen; i++) {
			if (0 == i) {
				_s[0] ^= (8389 % 128);
			} else if (_s[i] != _s[i - 1]) {
				_s[i] ^= _s[i - 1];
			}
		}

		int j = strLen - 1;
		for (i = j; i > -1; i--) {
			if (i == j) {
				_s[i] ^= (84322 % 128);
			} else if (_s[i] != _s[i + 1]) {
				_s[i] ^= _s[i + 1];
			}
		}
		String result = "";
		for (i = 0; i < strLen; i++) {

			result += Integer.toHexString((0x000000ff & _s[i]) | 0xffffff00)
					.substring(6);
		}
		return result;
	}

	public static String b(String s) {
		if (TextUtils.isEmpty(s)) {
			return null;
		}
		String str = "";
		try {
			String code = s;
			StringBuffer sb = new StringBuffer(code);
			ByteBuffer bb = ByteBuffer.allocate(code.length() / 2);

			int i = 0;
			while (i < code.length()) {
				bb.put((byte) ((Integer.parseInt(sb.substring(i, i + 1), 16)) << 4 | Integer
						.parseInt(sb.substring(i + 1, i + 2), 16)));
				i = i + 2;
			}

			byte cCode[] = bb.array();

			for (i = 0; i < cCode.length; i++) {
				if (i == cCode.length - 1) {
					cCode[cCode.length - 1] ^= (84322 % 128);
				} else if (cCode[i] != cCode[i + 1]) {
					cCode[i] ^= cCode[i + 1];
				}
			}
			for (i = cCode.length - 1; i >= 0; i--) {
				if (i == 0) {
					cCode[0] ^= (8389 % 128);
				} else if (cCode[i] != cCode[i - 1]) {
					cCode[i] ^= cCode[i - 1];
				}
			}
			str = new String(cCode, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}
	
	public static String SceneList2String(List<String> SceneList) {
		if(SceneList == null && SceneList.size() == 0)
			return "";
		String SceneListString = "";
		try {
		      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		      objectOutputStream.writeObject(SceneList);
		      SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		      objectOutputStream.close();
		} catch (IOException e) {
		}
      return SceneListString;
	}
 
	public static List<String> String2SceneList(String SceneListString){
		if(TextUtils.isEmpty(SceneListString))
			return null;
		List<String> SceneList = new ArrayList<String>();
		try {
		      byte[] mobileBytes = Base64.decode(SceneListString.getBytes(), Base64.DEFAULT);
		      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		      SceneList = (List<String>) objectInputStream.readObject();
		      objectInputStream.close();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
      return SceneList;
	}
	
	public static String pageUrl(String objectID){
		return "http://meetu.avosapps.com/m/journeyandroid.html?id="+objectID;
	}
	
	public static void hideInputMethod(Activity mActivity) {
		if (mActivity.getCurrentFocus() != null) {
			((InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getWindow()
					.peekDecorView().getWindowToken(), 0);
		}
	}
}
