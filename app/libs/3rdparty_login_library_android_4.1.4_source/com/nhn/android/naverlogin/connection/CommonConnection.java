package com.nhn.android.naverlogin.connection;

import javax.net.ssl.SSLPeerUnverifiedException;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.connection.ResponseData.ResponseDataStat;
import com.nhn.android.naverlogin.util.CookieUtil;
import com.nhn.android.naverlogin.util.DeviceAppInfo;

import android.content.Context;
import android.util.Log;
/*
 * 서버 request시 사용할 수 있는 class
 * URL로 request하고 response를 리턴해주는 method 모음 
 * @author naver
 *
 */
public class CommonConnection {
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "CommonConnection";
	
	// httpClient 가 사용 중이면 --> ReponseDataStat.BUSY 를 리턴함
	protected static DefaultHttpClient mHttpClient = null;
	protected static boolean mCancel;
 
	/**
	 * URL 로 request 후 reponse로 ResponseData를 리턴
	 * @param context context
	 * @param strRequestUrl request url
	 * @param cookies cookie string 
	 * @param userAgent useragent
	 * @return : url request 로 얻은 response data를 리턴. response data는 content 와 status-code, cookie 로 구성됨
	 */
	public static ResponseData request(Context context, String strRequestUrl, String cookies, String userAgent) {
		return request(context, strRequestUrl, cookies, userAgent, false);
	}
	

	public static ResponseData request(Context context, String strRequestUrl, String cookies, String userAgent, String authHeader) {
		return request(context, strRequestUrl, cookies, userAgent, authHeader, false); 
	}
 
	public static ResponseData request(Context context, String strRequestUrl, String cookies, String userAgent, boolean httpClientIsolated) {
		return request(context, strRequestUrl, cookies, userAgent, null, httpClientIsolated);
	}
	
	/**
	 * request then, return response
	 * @param context context
	 * @param strRequestUrl request url
	 * @param cookies cookie string 
	 * @param userAgent useragent
	 * @param authHeader if need inserting authHeader, fill this field.
	 * @param httpClientIsolated httpClient 를 독립적으로 사용하는지 여부 결정, 독립적으로 사용하는 경우 cancel 불가
	 * @return responseData
	 */
	public static ResponseData request(Context context, String strRequestUrl, String cookies, String userAgent, String authHeader, boolean httpClientIsolated) {
		HttpGet httpget = null;
		HttpResponse httpResponse = null;
		ResponseData res = new ResponseData();
		String postCookies = null;
		
		DefaultHttpClient httpClient = null;
		
		synchronized(CommonConnection.class) {
			
			if (httpClientIsolated) {
				
			} else {
				if (mHttpClient != null) {
					res.setResultCode(ResponseDataStat.BUSY, "HttpClient already in use.");
					return res;
				}
			}

			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "request url : " + strRequestUrl);
			}
			
			
			if (strRequestUrl == null || strRequestUrl.length() == 0) {
				res.setResultCode(ResponseDataStat.URL_ERROR, "strRequestUrl is null");
				return res;  
			}
	
			// HttpClient 설정 
			if (httpClientIsolated) {
				if (userAgent != null && userAgent.length() > 0) {
					httpClient = getDefaultHttpClient(userAgent);
				} else {
					httpClient = getDefaultHttpClient(context);
				}	
			} else {
				if (userAgent != null && userAgent.length() > 0) {
					mHttpClient = getDefaultHttpClient(userAgent);
				} else {
					mHttpClient = getDefaultHttpClient(context);
				}	
			}
			
			mCancel = false;
			
		}
		
		try {
			httpget = new HttpGet(strRequestUrl);
			if (cookies != null && cookies.length() > 0) {
				httpget.addHeader("Cookie", cookies);
			}
			if (null != authHeader) {
				httpget.removeHeaders("Authorization");
				httpget.setHeader("Authorization", authHeader);

				if (OAuthLoginDefine.DEVELOPER_VERSION) {
					Log.d(TAG, "header:" + authHeader);
				}
			}
			if (httpClientIsolated) {
				httpResponse = httpClient.execute(httpget);
			} else {
				httpResponse = mHttpClient.execute(httpget);
			}
		} catch (ConnectTimeoutException e) {
			res.setResultCode(ResponseDataStat.CONNECTION_TIMEOUT, e.getMessage());
			e.printStackTrace();
		} catch (SSLPeerUnverifiedException e) {
			res.setResultCode(ResponseDataStat.NO_PEER_CERTIFICATE, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			res.setResultCode(ResponseDataStat.CONNECTION_FAIL, e.getMessage());
			e.printStackTrace();
		}

		try {
			if (httpResponse != null) {
				postCookies = CookieUtil.getCookie(httpResponse);
				res.setResponseData(httpResponse, postCookies);
			}
		} catch(Exception e) {
			res.setResultCode(ResponseDataStat.FAIL, "setResponseData() on request() failed :" + e.getMessage());
			e.printStackTrace();
		}

		try {
			if (httpClientIsolated) {
				httpClient.getConnectionManager().shutdown();
			} else {
				mHttpClient.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClientIsolated) {
				httpClient = null;
			} else {
				mHttpClient = null;
			}
		}
		
		if (mCancel) {
			ResponseData cc = new ResponseData();
			cc.setResultCode(ResponseDataStat.CANCEL, "User cancel");
			return cc;
		}

		return res;
	}

	
	/**
	 * 로그인 모듈 user-agent 가 설정된 http client 를 리턴
	 * @param context context
	 * @return default http client
	 */
	public static DefaultHttpClient getDefaultHttpClient(Context context) {
		String useragent = DeviceAppInfo.getBaseInstance().getUserAgent(context);
		return getDefaultHttpClient(useragent);
	}

	protected static DefaultHttpClient getDefaultHttpClient(String useragent) {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpParams params = httpClient.getParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, useragent);

		if (OAuthLoginDefine.DEVELOPER_VERSION) {
			Log.d(TAG, "user-agent:" + useragent);
		}

		return httpClient;
	}
	
	private static DefaultHttpClient getDefaultHttpClient() {
		DefaultHttpClient httpClient = null;
		
		httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
				
		// set time-out
		HttpConnectionParams.setConnectionTimeout(params, OAuthLoginDefine.TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, OAuthLoginDefine.TIMEOUT);
		ConnManagerParams.setTimeout(params, OAuthLoginDefine.TIMEOUT);
		
		return httpClient;
	}

	public static boolean isBusy() {
		if (mHttpClient != null)
			return true;
		return false;
	}
	
	
	public static void cancel() {
		mCancel = true;
		if (mHttpClient != null) {
			Log.e(TAG, "httpclient operation canceled (shutdown)");
			mHttpClient.getConnectionManager().shutdown();
			mHttpClient = null;
		}
		// executor 는 cancel 안해줌 -> 동작이 복잡해짐 
	}

}
