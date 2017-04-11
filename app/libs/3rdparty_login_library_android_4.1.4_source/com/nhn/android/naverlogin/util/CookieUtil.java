package com.nhn.android.naverlogin.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.util.Log;
import com.nhn.android.naverlogin.OAuthLoginDefine;

public class CookieUtil {

	private static final String TAG = OAuthLoginDefine.LOG_TAG + "CookieUtil";

	public static final String COOKIE_DOMAIN_NID = "https://nid.naver.com";
	
	/**
	 * http-response 로 부터 cookie 를 받아옴 
	 * @param httpResponse response 데이터 
	 * @return cookie string 
	 */
	public static String getCookie(HttpResponse httpResponse) {
		StringBuilder cookie = new StringBuilder();
		Header[] header = httpResponse.getHeaders("Set-Cookie");

		cookie.setLength(0);
		
		if (header != null) {
			for (int i = 0; i < header.length; ++i) {
				cookie.append(header[i].getValue());
				
				String cookieNoSpace = cookie.toString().trim();
				
				if (!cookieNoSpace.endsWith(";")) {
					cookie.append(";");
				}
				
				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "cookie:" + header[i].getValue());
				}
			}
		}
		
		return cookie.toString();
	}
}
