package com.nhn.android.naverlogin.connection;

import com.nhn.android.naverlogin.data.OAuthLoginString;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * network의 상태 확인 
 * @author NAVER 
 *
 */
public class NetworkState {

	static public boolean isDataConnected(Context context) {
		// false 가 기본값인게 맞지만, 외부앱에서 쓰는 SDK 이기 때문에 ACCESS_NETWORK_STATE 퍼미션에 대한 확신을 못하기 때문에 exception 발생시엔 true 리턴함
		boolean connected = true;
		try {
			ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null) {
				connected = manager.getActiveNetworkInfo().isConnected();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return connected;
	}

	static public boolean is3GConnected(Context context) {
		NetworkInfo mobile = null;
		try {
			ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobile != null && mobile.isConnected()) {
					return (true);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return (false);
	}

	static public boolean isWifiConnected(Context context) {
		NetworkInfo wifi = null;
		try {
			ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifi.isConnected()) {
					return (true);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return (false);
	}
	
	static boolean shown = false;
	
	public static void showRetry(final Context context, final RetryListener retryListener) {
		if (shown || context == null) {
			return;
		}
		if (context instanceof Activity) {
			if (((Activity)context).isFinishing()) {
				return;
			}
		}
		shown = true;
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setMessage(OAuthLoginString.naveroauthlogin_string_network_state_not_available.getString(context));
		dialog.setPositiveButton(OAuthLoginString.naveroauthlogin_string_msg_retry.getString(context), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				shown = false;
				if (context instanceof Activity) {
					if (((Activity)context).isFinishing()) {
						return;
					}
				}
				retryListener.onResult(true);
			}

		});
		dialog.setNegativeButton(OAuthLoginString.naveroauthlogin_string_msg_cancel.getString(context), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				shown = false;
				if (context instanceof Activity) {
					if (((Activity)context).isFinishing()) {
						return;
					}
				}
				retryListener.onResult(false);
			}

		});
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				shown = false;
				if (context instanceof Activity) {
					if (((Activity)context).isFinishing()) {
						return;
					}
				}
				retryListener.onResult(false);
			}

		});

		try {
			dialog.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean checkConnectivity(Context context, boolean showDialog, final RetryListener retryListener) {
		if (isDataConnected(context)) {
			return (true);
		}

		if (showDialog) {
			if (retryListener == null) {
				String msg = OAuthLoginString.naveroauthlogin_string_network_state_not_available.getString(context);
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				return false;
			}
			
			showRetry(context, retryListener);
		}
		return (false);
	}
	
	
	/**
	 * RetryListener
	 */
	public interface RetryListener {
		public void onResult(boolean retry);
	}


	public static String getNetworkState(Context context) {
		String network = "other";
		
		if (NetworkState.is3GConnected(context)) {
			network = "cell";
		} else if (NetworkState.isWifiConnected(context)) {
			network = "wifi";
		}
		
		return network;
	}
}

