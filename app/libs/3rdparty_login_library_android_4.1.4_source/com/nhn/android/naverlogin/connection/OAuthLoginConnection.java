package com.nhn.android.naverlogin.connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.connection.gen.OAuthQueryGenerator;
import com.nhn.android.naverlogin.data.OAuthErrorCode;
import com.nhn.android.naverlogin.data.OAuthResponse;
import com.nhn.android.naverlogin.util.DeviceAppInfo;

import android.content.Context;
import android.util.Log;

public class OAuthLoginConnection extends CommonConnection {

    private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthLoginConnection";
    /**
     * access token 요청
     * @param context context
     * @param clientId client id
     * @param clientSecret client secret
     * @param initState oauth2.0 에서의 state 값 (random seed)
     * @param code oauth2.0 에서의 code 값  (intermidate auth code)
     * @return oauth response 
     */
    public static OAuthResponse requestAccessToken(Context context, String clientId, String clientSecret, String initState, String code) {
    	String locale = DeviceAppInfo.getBaseInstance().getLocaleString(context);
   	   	String requestUrl = new OAuthQueryGenerator().generateRequestAccessTokenUrl(clientId, clientSecret, initState, code, locale);
    	return request(context, requestUrl);
    }

	
	public static OAuthResponse requestRefreshToken(Context context, String clientId, String clientSecret, String refreshToken) {
		String locale = DeviceAppInfo.getBaseInstance().getLocaleString(context);
		String requestUrl = new OAuthQueryGenerator().generateRequestRefreshAccessTokenUrl(clientId, clientSecret, refreshToken, locale);
    	return request(context, requestUrl);		
	}

	public static OAuthResponse deleteToken(Context context, String clientId, String clientSecret, String accessToken) {
		String locale = DeviceAppInfo.getBaseInstance().getLocaleString(context);
		String requestUrl = new OAuthQueryGenerator().generateRequestDeleteAccessTokenUrl(clientId, clientSecret, accessToken, locale);
    	return request(context, requestUrl);				
	}
	
	
	private static OAuthResponse request(Context context, String requestUrl) {
		ResponseData data = CommonConnection.request(context, requestUrl, null, null);
    	

		if (!(data.mStat.equals(ResponseData.ResponseDataStat.SUCCESS))) {
			if (data.mStatusCode == 503) {
				return new OAuthResponse(OAuthErrorCode.SERVER_ERROR_TEMPORARILY_UNAVAILABLE);
			} else if (data.mStatusCode == 500) {
				return new OAuthResponse(OAuthErrorCode.SERVER_ERROR_SERVER_ERROR);
			} else if (data.mStat.equals(ResponseData.ResponseDataStat.CONNECTION_TIMEOUT)
					|| data.mStat.equals(ResponseData.ResponseDataStat.CONNECTION_FAIL)){
				return new OAuthResponse(OAuthErrorCode.CLIENT_ERROR_CONNECTION_ERROR);
			} else if (data.mStat.equals(ResponseData.ResponseDataStat.NO_PEER_CERTIFICATE)){
				return new OAuthResponse(OAuthErrorCode.CLIENT_ERROR_CERTIFICATION_ERROR);
			} else {
				return new OAuthResponse(OAuthErrorCode.ERROR_NO_CATAGORIZED);
			}
		}
    	
		try {
			JSONObject jsonObj = new JSONObject(data.mContent);

			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "len :" + jsonObj.length());
				Log.d(TAG, "str :" + jsonObj.toString());
			}
			
			Iterator it = jsonObj.keys();
			Map<String, String> ret = new HashMap<String, String>();
			
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = jsonObj.getString(key);
				ret.put(key, value);

				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "key:" + key + ",value:" + value);
				}
			}

			return new OAuthResponse(ret);
    		
		} catch (JSONException e) {
			if (OAuthLoginDefine.DEVELOPER_VERSION && null != data) {
				Log.d(TAG, "content:" + data.mContent);
			}
			e.printStackTrace();
		}
		
		return new OAuthResponse(OAuthErrorCode.CLIENT_ERROR_PARSING_FAIL);
	}

	/**
	 * naver id 에 해당하는 token 과 token secret 을 얻어와 OAuth1.0a 의 인증을 위한 헤더를 포함하여 요청한다.
	 * @param context context
	 * @param requestUrl request url
	 * @param cookie cookie
	 * @param userAgent user-agent string 
	 * @param authHeader Authorization Header string 
	 * @return response data
	 */
	public static ResponseData requestWithOAuthHeader(Context context, String requestUrl, String cookie, String userAgent, String authHeader) {
		ResponseData data = CommonConnection.request(context, requestUrl, null, null, authHeader, false);
		return data;
	}
}
