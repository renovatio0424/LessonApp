package com.nhn.android.naverlogin.ui;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.connection.OAuthLoginConnection;
import com.nhn.android.naverlogin.data.OAuthErrorCode;
import com.nhn.android.naverlogin.data.OAuthIntent;
import com.nhn.android.naverlogin.data.OAuthLoginData;
import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager;
import com.nhn.android.naverlogin.data.OAuthLoginString;
import com.nhn.android.naverlogin.data.OAuthResponse;
import com.nhn.android.naverlogin.ui.OAuthLoginInAppBrowserActivity.OAuthLoginInAppBrowserInIntentData;
import com.nhn.android.naverlogin.util.DeviceAppInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * refresh token 이 있는 경우 refresh token 으로 access token 을 가져옴 
 * access token 가져오는 걸 실패하는 경우엔 로그인 창을 보여줌 (네이버앱을 통하거나, 직접 webview를 생성해서..)
 * @author naver
 *
 */
public class OAuthLoginActivity extends Activity {
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthLoginActivity";
	private static int REQUESTCODE_LOGIN = 100;
	
	// dialog
	private OAuthLoginDialogMng mDialogMng = new OAuthLoginDialogMng();
	
	
	private Context 			mContext;
	private OAuthLoginData 		mOAuthLoginData;
	private String 				mClientName;

	private boolean 			mIsLoginActivityStarted = false;

	
	
	private boolean initData(Bundle savedInstanceState) {

		mContext = OAuthLoginActivity.this;
		
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(mContext);
		String clientId = pref.getClientId();
		String clientSecret = pref.getClientSecret();
		String callbackUrl = pref.getCallbackUrl();
		String state = (null == savedInstanceState) ? null : savedInstanceState.getString("OAuthLoginData_state");
		
		mClientName = pref.getClientName();
		
		if (TextUtils.isEmpty(clientId)) {
			finishWithErrorResult(OAuthErrorCode.CLIENT_ERROR_NO_CLIENTID);
			return false;
		}
		if (TextUtils.isEmpty(clientSecret)) {
			finishWithErrorResult(OAuthErrorCode.CLIENT_ERROR_NO_CLIENTSECRET);
			return false;
		}
		if (TextUtils.isEmpty(mClientName)) {
			finishWithErrorResult(OAuthErrorCode.CLIENT_ERROR_NO_CLIENTNAME);
			return false;
		}
		if (TextUtils.isEmpty(callbackUrl)) {
			finishWithErrorResult(OAuthErrorCode.CLIENT_ERROR_NO_CALLBACKURL);
			return false;
		}

		mOAuthLoginData = new OAuthLoginData(clientId, clientSecret, callbackUrl, state);

		return true;
	}
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "onCreate()");
		}
		
		if (false == initData(savedInstanceState)) {
			return ;
		}

		if (null != savedInstanceState) {
			mIsLoginActivityStarted = savedInstanceState.getBoolean("IsLoginActivityStarted");
		}
		

		if (!mIsLoginActivityStarted) {
			mIsLoginActivityStarted = true;
			
			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "onCreate() first");
			}
			
			runOnlyOnce();
		}
		
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "onRestoreInstanceState()");
		}
		
		if (null != savedInstanceState) {
			mIsLoginActivityStarted = savedInstanceState.getBoolean("IsLoginActivityStarted");
		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "Login onSaveInstanceState()");
		}
		
		outState.putBoolean("IsLoginActivityStarted", mIsLoginActivityStarted);
		outState.putString("OAuthLoginData_state", mOAuthLoginData.getInitState());

	}
	

	private void startLoginActivity() {
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "startLoginActivity()");
		}
		
		try {
			Intent intent = new Intent();
			
			intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_CLIENT_ID, mOAuthLoginData.getClientId());
			intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_CALLBACK_URL, mOAuthLoginData.getCallbackUrl());
			intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_STATE, mOAuthLoginData.getInitState());
			intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_APP_NAME, mClientName);
			intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_OAUTH_SDK_VERSION, OAuthLoginDefine.VERSION);
			
			if (DeviceAppInfo.isIntentFilterExist(mContext, OAuthLoginDefine.ACTION_OAUTH_LOGIN)) {

				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "startLoginActivity() with naapp");	
				}
				
				// 네이버앱에서 처리 가능한 intent
				intent.setPackage(OAuthLoginDefine.NAVER_PACKAGE_NAME); 
				intent.setAction(OAuthLoginDefine.ACTION_OAUTH_LOGIN);
				startActivityForResult(intent, REQUESTCODE_LOGIN);
				return ;
			} else if (DeviceAppInfo.isIntentFilterExist(mContext, OAuthLoginDefine.ACTION_OAUTH_LOGIN_2NDAPP)) {

				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "startLoginActivity() with appstore");	
				}
				
				intent.setAction(OAuthLoginDefine.ACTION_OAUTH_LOGIN_2NDAPP);

				String appName = DeviceAppInfo.getPrimaryNaverOAuth2ndAppPackageName(mContext);
				if (!TextUtils.isEmpty(appName)) {
					intent.setPackage(appName);
				}
 
				startActivityForResult(intent, REQUESTCODE_LOGIN);
				return ;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "startLoginActivity() with webview");
		}
		
		// inapp browser 를 통해 기존 로그인 방식으로 로그인한다.
		startLoginWebviewActivity((Activity)mContext, REQUESTCODE_LOGIN, mOAuthLoginData.getClientId(), mOAuthLoginData.getInitState(), mOAuthLoginData.getCallbackUrl());
		return ;
		
	}

	/**
	 * login webview 를 실행함
	 * 네이버앱이 없는 경우 대체 로그인 방식으로 사용할 수 있음  
	 */
	private void startLoginWebviewActivity(Activity callerActivity, int requestCode, String clientId, String state, String callbackUrl) {
		Intent intent = new Intent(callerActivity, OAuthLoginInAppBrowserActivity.class);
		intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_CLIENT_ID, clientId);
		intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_CALLBACK_URL, callbackUrl);
		intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_STATE, state);
		intent.putExtra(OAuthLoginInAppBrowserInIntentData.INTENT_PARAM_KEY_OAUTH_SDK_VERSION, OAuthLoginDefine.VERSION);
				
		callerActivity.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "onResume()");				
		}		
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "onPause()");	
		}		
	}

	private void runOnlyOnce() {
		if (mOAuthLoginData == null) {
			finishWithErrorResult(OAuthErrorCode.CLIENT_ERROR_NO_CLIENTID);
			return ;
		}

		startLoginActivity();
	}
	
	
	private void finishWithErrorResult(OAuthErrorCode errCode) {
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d("GILSUB", "Login finishWithErrorResult()");	
		}		
		
		Intent intent = new Intent();
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(mContext);
		
		prefMng.setLastErrorCode(errCode);
		prefMng.setLastErrorDesc(errCode.getDesc());

		intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE, errCode.getCode());
		intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION, errCode.getDesc());
		
    	setResult(RESULT_CANCELED, intent);
    	finish();

    	propagationResult(false);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
		
	    if (data == null) {
	    	finishWithErrorResult(OAuthErrorCode.CLIENT_USER_CANCEL);
	    	return ;
	    }

    	String state = data.getStringExtra(OAuthIntent.EXTRA_OAUTH_STATE);
    	String code = data.getStringExtra(OAuthIntent.EXTRA_OAUTH_CODE);
    	String errorCode = data.getStringExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE);
    	String errorDesc = data.getStringExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION);
    	
    	mOAuthLoginData.setMiddleResult(code, state, errorCode, errorDesc);

    	if (OAuthLoginDefine.DEVELOPER_VERSION) {
    		Log.d("GILSUB", "Login onActivityResult()");
    	}
		
    	if (TextUtils.isEmpty(code)) {
			OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(mContext);
			
			prefMng.setLastErrorCode(OAuthErrorCode.fromString(errorCode));
			prefMng.setLastErrorDesc(errorDesc);

	    	setResult(RESULT_CANCELED, data);
 	    	finish();
 	    	
	    	propagationResult(false);
	    	return ;
    		
    	} else {
    		new GetAccessTokenTask().execute();
    	}
	}
	


	private class GetAccessTokenTask extends AsyncTask<Void, Void, OAuthResponse> {
		@Override
		protected void onPreExecute() {
			try {
				mDialogMng.showProgressDlg(mContext, OAuthLoginString.naveroauthlogin_string_getting_token.getString(mContext), null);
			} catch (Exception e) {
				// do nothing
			}
		}
		
		@Override
		protected OAuthResponse doInBackground(Void... params) {
			try {
				return OAuthLoginConnection.requestAccessToken(mContext, 
									mOAuthLoginData.getClientId(), 
									mOAuthLoginData.getClientSecret(), 
									mOAuthLoginData.getState(), mOAuthLoginData.getCode());
			} catch (Exception e) {
				return new OAuthResponse(OAuthErrorCode.CLIENT_ERROR_CONNECTION_ERROR);
			}
		}
		
		protected void onPostExecute(OAuthResponse res) {
			try {
				mDialogMng.hideProgressDlg();
			} catch (Exception e) {
				// do nothing
			}
		    
			try {
				Intent intent = new Intent();
				OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(mContext);
				
				if (res.isSuccess()) {
					prefMng.setAccessToken(res.getAccessToken());
					prefMng.setRefreshToken(res.getRefreshToken());
					prefMng.setExpiresAt(System.currentTimeMillis() / 1000 + res.getExpiresIn());
					prefMng.setTokenType(res.getTokenType());
					prefMng.setLastErrorCode(OAuthErrorCode.NONE);
					prefMng.setLastErrorDesc(OAuthErrorCode.NONE.getDesc());
					
					intent.putExtra(OAuthIntent.EXTRA_OAUTH_ACCESS_TOKEN, res.getAccessToken());
					intent.putExtra(OAuthIntent.EXTRA_OAUTH_REFRESH_TOKEN, res.getRefreshToken());
					intent.putExtra(OAuthIntent.EXTRA_OAUTH_EXPIRES_IN, res.getExpiresIn());
					intent.putExtra(OAuthIntent.EXTRA_OAUTH_TOKEN_TYPE, res.getTokenType());
					setResult(RESULT_OK, intent);
				} else {
					if (res.getErrorCode() == OAuthErrorCode.NONE) {
				    	finishWithErrorResult(OAuthErrorCode.CLIENT_USER_CANCEL);
				    	return ;
					} else {
						prefMng.setLastErrorCode(res.getErrorCode());
						prefMng.setLastErrorDesc(res.getErrorDesc());
						intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE, res.getErrorCode().getCode());
						intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION, res.getErrorDesc());
						setResult(RESULT_CANCELED, intent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		    finish();
			
			try {
			    propagationResult(res.isSuccess());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}

	private void propagationResult(boolean b) {
		if (OAuthLogin.mOAuthLoginHandler != null) {
			Message msg = new Message();
			msg.what = b ? 1 : 0;
			OAuthLogin.mOAuthLoginHandler.sendMessage(msg);
		}
		

	}
	
}
