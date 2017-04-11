package com.nhn.android.naverlogin.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.data.OAuthErrorCode;
import com.nhn.android.naverlogin.data.OAuthIntent;
import com.nhn.android.naverlogin.data.OAuthLoginData;
import com.nhn.android.naverlogin.data.OAuthLoginPreferenceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class OAuthWebviewUrlUtil {

	
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthWebvewUrlUtil" ;

	public static final String FINAL_URL 			= "http://nid.naver.com/com.nhn.login_global/inweb/finish";
	public static final String FINAL_URL_HTTPS		= "https://nid.naver.com/com.nhn.login_global/inweb/finish";

	private static Map<String, String> getQueryMapFromUrl(String url) {
		
		if (url.contains("?")) {
			url = url.split("\\?")[1];
		}
		
		return getQueryMap(url);
	}
	
	private static Map<String, String> getQueryMap(String query)  
	{  
		if (query == null){
			return null;
		}
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {
	    	String[] keyname = param.split("=");
	    	
	    	if (keyname.length == 2) {
	    		String name = keyname[0];  
	    		String value = keyname[1];  
	    		map.put(name, value);
	    	} else if (keyname.length == 1){
	    		String name = keyname[0]; 
	    		String value = "";
	    		map.put(name, value);
	    	}
	    }  
	    return map;  
	}  
	
	/*
	 * 서버에서 네이버 token의 invalid 요청했는지 확인
	 */
	public static boolean isErrorResultNaverTokenInvalid(Context context, String preUrlString, String urlString, OAuthLoginData oAuthLoginData) {

		//String callbackUrl = oAuthLoginData.getCallbackUrl();
		
		try {
			if (!TextUtils.isEmpty(preUrlString) && (preUrlString.startsWith("https://nid.naver.com/") == false)) {
				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "isErrorResultNaverTokenInvalid - pre url is not naver.com");
				}
			/*} else if (null != callbackUrl 
					&& !(urlString.startsWith(callbackUrl))
					&& !urlString.startsWith("https://nid.naver.com/inform/inform_404.html")) {
				Log.d(TAG, "isErrorResultNaverTokenInvalid - current url is difference with callback url pre-defined");*/
			} else {
				Map<String, String> querymap = null;
				try {
					URL url = new URL(urlString);
					querymap = getQueryMap(url.getQuery());
				} catch (Exception e) {
					querymap = getQueryMapFromUrl(urlString);
				}
				if (null != querymap 
						&& querymap.containsKey("error") 
						&& querymap.containsKey("error_description")) {
					if (querymap.get("error").equalsIgnoreCase("invalid_request") 
							&& (querymap.get("error_description").contains("token")
											&& querymap.get("error_description").contains("invalid"))) {
						return true;
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static String getDecodedString(String oriStr) {

		if (TextUtils.isEmpty(oriStr)) {
			return oriStr;
		}
		
		String decodedStr = "";
		try {
			decodedStr = URLDecoder.decode(oriStr, "UTF-8");
		} catch (Exception e) {
			// do nothing
		}
		if (!TextUtils.isEmpty(decodedStr) 
				&& !decodedStr.equalsIgnoreCase(oriStr)) {
			return decodedStr;
		}
		return oriStr;
	}
	
	/**
	 * 
	 * 현재 url에 oauth 인증 완료를 알리는 값인 code와 state 값이 있으면 현재 activity를 종료해주고,  
	 * 사용자가 동의창에서 취소를 눌렀을 경우 현재 activity를 종료해준다.

	 * @param context context
	 * @param preUrlString 조금전 진입한 url  
	 * @param urlString	현재 url
	 * @param oAuthLoginData login 시 생성되는 혹은 필요한 데이터 
	 * @return	if oauth_verifier value exist, return true. else false.
	 */
	public static boolean returnWhenAuthorizationDone(Context context, String preUrlString, String urlString, OAuthLoginData oAuthLoginData) {
		boolean rt = false;

		if (oAuthLoginData == null) {
			oAuthLoginData = new OAuthLoginData("", "", "");
		}
		
		try {
			//String callbackUrl = oAuthLoginData.getCallbackUrl();
			if (!TextUtils.isEmpty(preUrlString) 
					&& ((preUrlString.startsWith("https://nid.naver.com/") || preUrlString.startsWith("https://nid.naver.com/")) == false)) {
				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "returnWhenAuthorizationDone - pre url is not naver.com");
				}
			} else if (urlString.startsWith("https://nid.naver.com/login/noauth/logout.nhn") 
					|| urlString.startsWith("http://nid.naver.com/nidlogin.logout")) {
				// 현재 창 종료 
				Intent intent = new Intent();
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE, OAuthErrorCode.CLIENT_USER_CANCEL.getCode());
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION, OAuthErrorCode.CLIENT_USER_CANCEL.getDesc());
				((Activity)context).setResult(Activity.RESULT_OK, intent);
				((Activity)context).finish();
				return true;
			/*} else if (null != callbackUrl
					&& !urlString.startsWith("https://nid.naver.com/inform/inform_404.html") 
					&& !(urlString.startsWith(callbackUrl))) {
				Log.d(TAG, "returnWhenAuthorizationDone - current url is difference with callback url pre-defined");*/
			} else {
				Map<String, String> querymap = null;
				try {
					URL url = new URL(urlString);
					querymap = getQueryMap(url.getQuery());
				} catch (Exception e) {
					querymap = getQueryMapFromUrl(urlString);
				}
				
				if (querymap != null && querymap.containsKey("code") && querymap.containsKey("state") ) {
					if (OAuthLoginDefine.DEVELOPER_VERSION) {
						Log.d(TAG, "query map contain code and state");
					}
					rt = true;
				} else if (querymap != null && querymap.containsKey("error") && querymap.containsKey("error_description")) {
					if (OAuthLoginDefine.DEVELOPER_VERSION) {
						Log.d(TAG, "query map contain error, url : " + urlString );
					}
					rt = true;
					OAuthLoginPreferenceManager pref = new OAuthLoginPreferenceManager(context);
					pref.setLastErrorCode(OAuthErrorCode.fromString(querymap.get("error")));
					String errorDesc = getDecodedString(querymap.get("error_description"));
					pref.setLastErrorDesc(errorDesc);
					
				} else {
					if (OAuthLoginDefine.DEVELOPER_VERSION) {
						Log.d(TAG, "query map does not contain code and state, url:" + urlString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rt) {
			try {
				// callback url 에서 code 와 state 값 등을 mOAuthLoginData에 저장함 
				processCallbackUrl(context, urlString, oAuthLoginData);
				
				Intent intent = new Intent();
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_CODE, oAuthLoginData.getCode());
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_STATE, oAuthLoginData.getInitState());
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE, oAuthLoginData.getErrorCode().getCode());
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION, oAuthLoginData.getErrorDesc());
				
				((Activity)context).setResult(Activity.RESULT_OK, intent);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				
				Intent intent = new Intent();				
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_CODE, OAuthErrorCode.CLIENT_ERROR_PARSING_FAIL.getCode());
				intent.putExtra(OAuthIntent.EXTRA_OAUTH_ERROR_DESCRIPTION, OAuthErrorCode.CLIENT_ERROR_PARSING_FAIL.getDesc());
				
				((Activity)context).setResult(Activity.RESULT_CANCELED, intent);				
			}
			

			((Activity)context).finish();
		}
		
		return rt;		
	}

	public static boolean isFinalUrl(boolean isShouldOverrideUrl, String preUrl, String url) {
		if (url == null) {
			return false;
		}
		if (url.equalsIgnoreCase(FINAL_URL) 
				|| url.equalsIgnoreCase(FINAL_URL_HTTPS)
				|| url.equalsIgnoreCase("http://m.naver.com/") 
				|| url.equalsIgnoreCase("http://m.naver.com") ) {
			return true;
		}
		
		// shoudOverrideUrl 에선 post method 로 넘어오는 건 처리 안함 (로그인의 경우) 
		if (true == isShouldOverrideUrl) {
			if (url.startsWith("https://nid.naver.com/nidlogin.login?svctype=262144") ) {
				return true;
			}
		}

		// 휴면 계정 (한글) -- https://nid.naver.com/mobile/user/help/sleepId.nhn?m=actionCheckSleepId -- 휴면해제 후 정상적인 로그인 페이지로 이동함 
		// 보호 조치 (영어) -- https://nid.naver.com/mobile/user/global/idSafetyRelease.nhn?m=viewInitPasswd&token_help=
		// 보호 조치 (한글) -- https://nid.naver.com/mobile/user/help/idSafetyRelease.nhn?m=viewInputPasswd&token_help=
		if (false == isShouldOverrideUrl 
				&& ((preUrl.startsWith("https://nid.naver.com/mobile/user/help/sleepId.nhn?m=viewSleepId&token_help=") 
							&& url.startsWith("https://nid.naver.com/nidlogin.login?svctype=262144"))
						|| (preUrl.startsWith("https://nid.naver.com/mobile/user/global/idSafetyRelease.nhn?") 
								&& url.startsWith("https://nid.naver.com/nidlogin.login?svctype=262144")) 
						|| (preUrl.startsWith("https://nid.naver.com/mobile/user/help/idSafetyRelease.nhn?") 
								&& url.startsWith("https://nid.naver.com/nidlogin.login?svctype=262144"))) ) {
			return true;
		}
		
		return false;
	}
	

    /*
     * callback url 에서 code 와 state 를 얻거나, error code 및 error description 을 얻는다.
     */
	public static void processCallbackUrl(Context context, String urlstr, OAuthLoginData data) throws MalformedURLException {
		Map<String, String> querymap = null;
		try {
			URL url = new URL(urlstr);
			querymap = getQueryMap(url.getQuery());
		} catch (Exception e) {
			querymap = getQueryMapFromUrl(urlstr);
		}
		
		

		String code = querymap.get("code");
		String state = querymap.get("state");
		String errorCode = querymap.get("error");
		String errorDesc = getDecodedString(querymap.get("error_description"));	//querymap.get("error_description");
		
		data.setMiddleResult(code, state, errorCode, errorDesc);
 
	}

}
