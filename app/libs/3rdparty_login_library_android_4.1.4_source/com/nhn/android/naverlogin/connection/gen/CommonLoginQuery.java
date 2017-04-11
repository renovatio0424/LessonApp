package com.nhn.android.naverlogin.connection.gen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;


public class CommonLoginQuery {

	protected final String kUrlRequestLoginKey 			= "https://nid.naver.com/nidlogin.login?";
	protected final String kUrlRequestLogout 			= "https://nid.naver.com/nidlogin.logout?";


	private static String percentEncode(String s) throws UnsupportedEncodingException {
		if (s == null) {
			return "";
		}
		return URLEncoder.encode(s, "UTF-8")
								// OAuth encodes some characters differently:
								.replace("+", "%20").replace("*", "%2A")
								.replace("%7E", "~");
	}

	protected String getQueryParameter(Map<String, String> paramArray) {
		Set<String> keys = paramArray.keySet();
		StringBuilder query = new StringBuilder("");
		String value;
		
		for (String key : keys) {
			value = paramArray.get(key);
			if (key == null || value == null) {
				continue;
			}
			if (query.length() > 0) {
				query.append("&");
			} 

			query.append(key);
			query.append("=");
			try {
				query.append(percentEncode(value));
			} catch (UnsupportedEncodingException e) {
				//e.printStackTrace();
				query.append(value);
			}

		}
		
		return query.toString();
	}
}
