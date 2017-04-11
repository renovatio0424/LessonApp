package com.nhn.android.naverlogin.connection.gen;

import java.util.HashMap;
import java.util.Map;

import com.nhn.android.naverlogin.OAuthLoginDefine;

public class OAuthQueryGenerator extends CommonLoginQuery {
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthQueryGenerator";

	private final static String OAUTH_REQUEST_AUTH_URL = "https://nid.naver.com/oauth2.0/authorize?";
	private final static String OAUTH_REQUEST_ACCESS_TOKEN_URL = "https://nid.naver.com/oauth2.0/token?";
	
	
	/**
	 * webview에서 인증 시작할때 쓸 url 을 만듬 
	 * @param clientId client id 
	 * @param state OAuth2.0 에서 쓰이는 state string (random seed)
	 * @param callbackUrl 완료 후 돌아갈 url 
	 * @param locale 언어값 
	 * @return generated url 
	 */
	public String generateRequestInitUrl(String clientId, String state, String callbackUrl, String locale) {
		return generateRequestInitUrl(clientId, state, callbackUrl, locale, null);
	} 

	public String generateRequestInitUrl(String clientId, String state, String callbackUrl, String locale, String network) {
		String url = "";
		Map<String, String> paramArray = new HashMap<String, String> ();

		paramArray.put("client_id", clientId);
		paramArray.put("response_type", "code");
		paramArray.put("inapp_view", "true");
		paramArray.put("oauth_os", "android");
		paramArray.put("locale", locale);
		if (null == network) {
			// do nothing
		} else {
			paramArray.put("network", network);
		}
		paramArray.put("redirect_uri", callbackUrl);		// getQueryParameter 에서 encoding 됨. 2014.11.27일 강병국님 메일로 수정됨 
		paramArray.put("state", state);
		
		url = String.format("%s%s", OAUTH_REQUEST_AUTH_URL, getQueryParameter(paramArray));

		return url;
	}

	public String generateRequestAccessTokenUrl(String clientId, String clientSecret, String state, String code, String locale) {

		String url = "";
		Map<String, String> paramArray = new HashMap<String, String> ();

		paramArray.put("client_id", clientId);
		paramArray.put("client_secret", clientSecret);
		paramArray.put("grant_type", "authorization_code");
		paramArray.put("state", state);
		paramArray.put("code", code);
		paramArray.put("oauth_os", "android");
		paramArray.put("locale", locale);


		url = String.format("%s%s", OAUTH_REQUEST_ACCESS_TOKEN_URL, getQueryParameter(paramArray));

		return url;
	}
	
	public String generateRequestRefreshAccessTokenUrl(String clientId, String clientSecret, String refreshToken, String locale) {

		String url = "";
		Map<String, String> paramArray = new HashMap<String, String> ();

		paramArray.put("client_id", clientId);
		paramArray.put("client_secret", clientSecret);
		paramArray.put("grant_type", "refresh_token");
		paramArray.put("refresh_token", refreshToken);
		paramArray.put("oauth_os", "android");
		paramArray.put("locale", locale);

		url = String.format("%s%s", OAUTH_REQUEST_ACCESS_TOKEN_URL, getQueryParameter(paramArray));

		return url;
	}

	public String generateRequestDeleteAccessTokenUrl(String clientId, String clientSecret, String accessToken, String locale) {

		String url = "";
		Map<String, String> paramArray = new HashMap<String, String> ();

		paramArray.put("client_id", clientId);
		paramArray.put("client_secret", clientSecret);
		paramArray.put("grant_type", "delete");
		paramArray.put("access_token", accessToken);
		paramArray.put("service_provider", "NAVER");
		paramArray.put("oauth_os", "android");
		paramArray.put("locale", locale);

		url = String.format("%s%s", OAUTH_REQUEST_ACCESS_TOKEN_URL, getQueryParameter(paramArray));

		return url;
	}
	
}
