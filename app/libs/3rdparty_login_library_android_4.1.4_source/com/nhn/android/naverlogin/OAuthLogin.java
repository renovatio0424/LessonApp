package com.nhn.android.naverlogin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.nhn.android.naverlogin.connection.CommonConnection;
import com.nhn.android.naverlogin.connection.NetworkState;
import com.nhn.android.naverlogin.connection.OAuthLoginConnection;
import com.nhn.android.naverlogin.connection.ResponseData;
import com.nhn.android.naverlogin.data.OAuthErrorCode;
import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.nhn.android.naverlogin.data.OAuthLoginString;
import com.nhn.android.naverlogin.data.OAuthResponse;
import com.nhn.android.naverlogin.ui.OAuthLoginActivity;
import com.nhn.android.naverlogin.ui.OAuthLoginDialogMng;
import com.nhn.android.naverlogin.util.DeviceAppInfo;

/**
 * 네이버아이디로로그인 SDK 를 사용할 때 주로 사용되는 class
 * - getInstance() 로 인스턴스를 받아서 필요한 동작을 수행한다.
 * - OAuth2.0 연동 / 연동 해제 / OpenAPI 호출 등이 가능하다.
 * @author naver
 *
 */
public class OAuthLogin {
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthLogin";
	private static OAuthLogin sInstance;
	
	public static OAuthLoginHandler mOAuthLoginHandler;
	
	/*
	 * 싱글턴 패턴을 이용하여 OAuthLogin 객체를 생성하여 리턴하거나 기존에 생성했던 걸 리턴한다.
	 */
	public static OAuthLogin getInstance() {
		if (sInstance == null) {
			sInstance = new OAuthLogin();
		}
		return sInstance;
	}
	
	private OAuthLogin() {
		// do nothing 
	}
	

	/// OAuth 인증시 필요한 값들을 preference에 저장함 
	/**
	 * OAuth 인증시 필요한 값들을 preference에 저장함. 2015년 8월 이후에 등록하여 package name 을 넣은 경우 사용.
	 * @param context shared Preference를 얻어올 때 사용할 context 
	 * @param clientId OAuth client id 값
	 * @param clientSecret OAuth client secret 값
	 * @param clientName OAuth client name 값 (네이버앱을 통한 로그인시 보여짐)
	 */
	public void init(Context context, String clientId, String clientSecret, String clientName) {
		String packageName = DeviceAppInfo.getPackageName(context);
		init(context, clientId, clientSecret, clientName, packageName);
	}
	
	
	/// OAuth 인증시 필요한 값들을 preference에 저장함 
	/**
	 * OAuth 인증시 필요한 값들을 preference에 저장함. 2015년 8월 이전 등록했고 그 뒤로 앱 정보 변경을 하지 않은 경우 사용.
	 * @param context shared Preference를 얻어올 때 사용할 context 
	 * @param clientId OAuth client id 값
	 * @param clientSecret OAuth client secret 값
	 * @param clientName OAuth client name 값 (네이버앱을 통한 로그인시 보여짐)
	 * @param callbackIntent 2015년 8월 이전에 등록한 사용자는 네아로 웹페이지에서 앱 등록시 넣어준 intent(callback url)를 넣어준다. 그 값과 다르면 인증을 실패한다.
	 */
	@Deprecated
	public void init(Context context, String clientId, String clientSecret, String clientName, String callbackIntent) {
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		
		prefMng.setClientId(clientId);
		prefMng.setClientSecret(clientSecret);
		
		prefMng.setClientName(clientName);
		prefMng.setCallbackUrl(callbackIntent);

		prefMng.setLastErrorCode(OAuthErrorCode.NONE);
		prefMng.setLastErrorDesc("");
	}
	
	
	private boolean valid(Context context) {
		if (null == context) {
			Log.i(TAG, "context is null");
			return false;			
		}
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		if (TextUtils.isEmpty(prefMng.getClientId())) {
			Log.i(TAG, "CliendId is null");
			return false;			
		}
		if (TextUtils.isEmpty(prefMng.getClientSecret())) {
			Log.i(TAG, "CliendSecret is null");
			return false;			
		}
		return true;
	}
	

	/// 네아로 SDK의 버전을 리턴한다
	public static String getVersion() {
		return OAuthLoginDefine.VERSION;
	}

	/// 네아로 인스턴스의 로그인 상태를 리턴해줌 
	/**
	 * @param context 저장된 access token 및 refresh token 을 얻어오기 위해 sharedPreference를 얻어올 때 쓰임
	 * @return {@link OAuthLoginState} ref
	 */
	public OAuthLoginState getState(Context context) {
		if (!valid(context)) {
			return OAuthLoginState.NEED_INIT;
		}
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		String at = prefMng.getAccessToken();
		String rt = prefMng.getRefreshToken();
		
		if (TextUtils.isEmpty(at)) {
			if (TextUtils.isEmpty(rt)) {
				return OAuthLoginState.NEED_LOGIN;
			} else {
				return OAuthLoginState.NEED_REFRESH_TOKEN;
			}
		}
		return OAuthLoginState.OK;
	}

	/// 네이버앱이 없는 경우 네이버앱을 설치하라는 배너를 띄울것인지 여부를 정해줌 
	public void setMarketLinkWorking(boolean set) {
		OAuthLoginDefine.MARKET_LINK_WORKING = set;
	}

	/// 네이버 아이디로 로그인(OAuth2.0) 연동을 해준다
	/**
	 * 로그인이 완료되면 oauthLoginHandler 가 호출된다.
	 * 이미 연동이 된 경우(shared preference에 refresh token 이 있는 경우)에는 access token 만 갱신해준다.
	 * @param activity 현재 메쏘드를 호출하는 activity의 context
	 * @param oauthLoginHandler 로그인 종료를 알릴 수 있는 handler
	 */
	public void startOauthLoginActivity(final Activity activity, final OAuthLoginHandler oauthLoginHandler) {
		if (NetworkState.checkConnectivity(activity, true, new NetworkState.RetryListener() {
			@Override
			public void onResult(boolean retry) {
				if (retry) {
					startOauthLoginActivity(activity, oauthLoginHandler);
				}
			}
		}) == true) {

			mOAuthLoginHandler = oauthLoginHandler;
			String rt = getRefreshToken(activity);
			
			if (!TextUtils.isEmpty(rt)) {
				new OAuthLoginTask(activity).execute();
			} else {
				Intent intent = new Intent(activity, OAuthLoginActivity.class);
				activity.startActivity(intent);
			}
		}
		
	}
	
	class OAuthNaverAppInstallMethod {
		static final int TYPE_UPDATE = 0x90;
		static final int TYPE_INSTALL = 0x91;
	}



	private class OAuthLoginTask extends AsyncTask<Void, Void, String> {
		private Context _context;
		private OAuthLoginDialogMng mDialogMng = new OAuthLoginDialogMng();
		
		OAuthLoginTask(Context context) {
			_context = context;
		}
		@Override
		protected void onPreExecute() {
			mDialogMng.showProgressDlg(_context, OAuthLoginString.naveroauthlogin_string_getting_token.getString(_context), null);
		}
		@Override
		protected String doInBackground(Void... params) {
			return refreshAccessToken(_context);
		}
		protected void onPostExecute(String at) {
			try {
				mDialogMng.hideProgressDlg();
			} catch (Exception e) {
				// do nothing
			}
			
			if (TextUtils.isEmpty(at)) {
				Intent intent = new Intent(_context, OAuthLoginActivity.class);
				_context.startActivity(intent);
			} else {
				mOAuthLoginHandler.run(true);
			}
		}
	}
	

	/**
	 * blocking / network method
	 * background 에서(asynctask, thread 등에서) 실행해야함
	 * shared preference에 저장된 갱신 토큰을 이용해 접근 토큰을 갱신하고 갱신된 접근 토큰을 반환한다
	 * 실패시 null을 리턴함.
	 * @param context context
	 * @return access token string (실패시 null을 리턴)
	 */
	public String refreshAccessToken(Context context) {
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		String clientId = prefMng.getClientId();
		String clientSecret = prefMng.getClientSecret();
		String refreshToken = prefMng.getRefreshToken();

		OAuthResponse res = OAuthLoginConnection.requestRefreshToken(context, clientId, clientSecret, refreshToken);
		
		String at = res.getAccessToken();
		if (TextUtils.isEmpty(at)) {
			return null;
		}
		
		prefMng.setAccessToken(res.getAccessToken());
		prefMng.setExpiresAt(System.currentTimeMillis() / 1000 + res.getExpiresIn());
		
		return at;
	}
	
	/// 로그인 결과로 얻어온 Access Token 을 리턴함 
	public String getAccessToken(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		String at = pref.getAccessToken();
		
		if (TextUtils.isEmpty(at)) {
			return null;
		}
		return at;
	}

	/// 로그인 결과로 얻어온 Refresh Token 을 리턴함
	public String getRefreshToken(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		String rt = pref.getRefreshToken();
		
		if (TextUtils.isEmpty(rt)) {
			return null;
		}
		return rt;
	}
	
	/// Access Token 의 만료 시간을 리턴함 
	public long getExpiresAt(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		long expiresAt = pref.getExpiresAt();

		return expiresAt;
	}
	
	/// 로그인 결과로 얻어온 Token의 Type을 리턴함
	public String getTokenType(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		String tokenType = pref.getTokenType();

		if (TextUtils.isEmpty(tokenType)) {
			return null;
		}
		return tokenType;
	}
	
	/// 지난 로그인 시도가 실패한 경우 Error code 를 리턴함
	public OAuthErrorCode getLastErrorCode(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		return pref.getLastErrorCode();
	}
	
	/// 지난 로그인 시도가 실패한 경우 Error description 을 리턴함
	public String getLastErrorDesc(Context context) {
		OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
		return pref.getLastErrorDesc();
	}
	
	/// API 를 호출하고 성공하는 경우 결과(content body)를 리턴함 
	/**
	 * blocking / network method
	 * background 에서(asynctask, thread 등에서) 실행해야함
	 * @param context context
	 * @param accessToken access token
	 * @param url url
	 * @return api 호출 결과 
	 */
	public String requestApi(Context context, String accessToken, String url) {
		String authHeader = "bearer " + accessToken;
		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "at:" + accessToken + ", url:" + url);
			Log.d(TAG, "header:" + authHeader);
		}
		ResponseData res = CommonConnection.request(context, url, null, null, authHeader);

		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "res.statuscode" + res.mStatusCode);
			Log.d(TAG, "res.content" + res.mContent);
		}

		if (res == null) {
			return null; 
		}
		
		return res.mContent;
	}


	/**
	 * blocking / network method
	 * background 에서(asynctask, thread 등에서) 실행해야함
	 * 클라이언트 및 서버에 저장되어 있는 Access token 및 Refresh token 을 삭제
	 * @param context context
	 * @return boolean true 서버에서 token 삭제 성공, false 서버에서 token 삭제 실패
	 */
	public boolean logoutAndDeleteToken(Context context) {
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		String clientId = prefMng.getClientId();
		String clientSecret = prefMng.getClientSecret();
		String accessToken = prefMng.getAccessToken();
		
		logout(context);
		
		OAuthResponse res = null;
		try {
			res = OAuthLoginConnection.deleteToken(context, clientId, clientSecret, accessToken);

			if ("success".equalsIgnoreCase(res.getResultValue())) {
				return true;
			}
			
			prefMng.setLastErrorCode(res.getErrorCode());
			prefMng.setLastErrorDesc(res.getErrorDesc());
			
			return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			prefMng.setLastErrorCode(OAuthErrorCode.ERROR_NO_CATAGORIZED);
			prefMng.setLastErrorDesc(e.getMessage());
			
			return false;
		}
	}
	
	
	/// 클라이언트에 저장되어 있는 Access token 및 Refresh token을 삭제함
	public void logout(Context context) {
		OAuthLoginPreferenceManager prefMng = new OAuthLoginPreferenceManager(context);
		
		prefMng.setAccessToken("");
		prefMng.setRefreshToken("");

		prefMng.setLastErrorCode(OAuthErrorCode.NONE);
		prefMng.setLastErrorDesc("");
	}
}
