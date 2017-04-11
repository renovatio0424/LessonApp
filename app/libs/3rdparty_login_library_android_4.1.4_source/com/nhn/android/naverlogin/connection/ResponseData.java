/// 로그인시 사용되는 데이터 클래스를 모아둔 package
/**
 * 로그인 결과 등의 서버에서 받은 결과를 파싱하여 저장할 수 있는 클래스가 있음 
 */
package com.nhn.android.naverlogin.connection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;

import com.nhn.android.naverlogin.OAuthLoginDefine;

import android.util.Log;

// <b> 로그인 결과의 세부 데이터를 저장하는 class </b>
/*
 * 서버의 response로 부터 login result를 생성
 * @author naver
 *
 */
public class ResponseData {
	private final String TAG = OAuthLoginDefine.LOG_TAG + "ResponseData";

	public ResponseDataStat 	mStat;
	public int 					mStatusCode;
	public String 				mContent;
	public String 				mCookie;
	public String 				mErrorDetail;
	
	public ResponseData() {
		mStat = ResponseDataStat.SUCCESS;
		mStatusCode = -1;
		mContent = "";
		mCookie = "";
		mErrorDetail = "";
	}
	
	public enum ResponseDataStat {
		SUCCESS				("SUCCESS", 		null), 				// 성공
		BUSY				("BUSY", 			"BUSY"),			// http client 가 사용중
		CANCEL				("CANCEL", 			"CANCEL"),	  		// 그외의 Exception 발생으로 실패
		URL_ERROR			("URL_ERROR", 		"URL_ERROR"),			// url 이 이상한 경우
		CONNECTION_TIMEOUT	("CONNECTION_TIMEOUT", "CONNECTION_TIMEOUT"), 		// timeout 발생한 경우
		CONNECTION_FAIL		("CONNECTION_FAIL", "CONNECTION_FAIL"),  			// connection 실패
		EXCEPTION_FAIL		("EXCEPTION_FAIL", 	"EXCEPTION_FAIL"),	  		// 그외의 Exception 발생으로 실패
		NO_PEER_CERTIFICATE	("NO_PEER_CERTIFICATE", "NO_PEER_CERTIFICATE"),		// 인증서오류
		FAIL				("FAIL", 			"FAIL");	  		// 그외의 실패
		
		private String stat;
		//private String mErrMsg;
		
		ResponseDataStat(String str, String errMsg) {
			stat = str;
			//mErrMsg = errMsg;
		}
		
		public String getValue() {
			return stat;
		}
	}
	


	public void setResultCode(ResponseDataStat stat, String msg) {
		mStat = stat;
		mErrorDetail = msg;
	}


	/*
	 * @param httpResponse : 유의! httpRespnse 는 httpConnection 을 close 하면 접근 불가
	 * @throws Exception 
	 */
	public void setResponseData(HttpResponse httpResponse, String cookie) {

		if (cookie != null) {
			mCookie = cookie;
		}
		
		if (httpResponse == null) {
			Log.e(TAG, "error : httpResponse is null !!");
			setResultCode(ResponseDataStat.FAIL, "setResponseData() - httpResponse is null");
			return ;
		}

		InputStream responseContent = null;
		try {
			responseContent = httpResponse.getEntity().getContent();
			mContent = getContent(responseContent);
			mStatusCode = httpResponse.getStatusLine().getStatusCode();

			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "headers:" + httpResponse.getAllHeaders().toString());
				Log.d(TAG, "status:" + httpResponse.getStatusLine().toString());
			}
		} catch (IllegalStateException e) {
			setResultCode(ResponseDataStat.EXCEPTION_FAIL, "setResponseData()-IllegalStateException:" + e.getMessage());
		} catch (Exception e) {
			setResultCode(ResponseDataStat.EXCEPTION_FAIL, "setResponseData()-Exception:" + e.getMessage());
		}
		
	}



	/**
	 * inputStream 에서 string 을 얻어냄 
	 */
	private String getContent(InputStream is) {
		final int bufferSize = 1024;
		char[] readString = new char[(int)bufferSize];
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			reader = new BufferedReader(new InputStreamReader(is));
		}
		
		int read;
		StringBuilder buf = new StringBuilder();
		try {
			while ((read = reader.read(readString, 0, bufferSize)) > 0) {
				buf.append(readString, 0, read);
			}
			reader.close();
		} catch (IOException e) {
			setResultCode(ResponseDataStat.EXCEPTION_FAIL, "getContent()-IOException:" + e.getMessage());
		} catch (RuntimeException e) {
			setResultCode(ResponseDataStat.EXCEPTION_FAIL, "getContent()-RuntimeException:" + e.getMessage());
		}		
		return buf.toString();
	}	

	@Override
	public String toString() {
		return "Statuscode:" + mStatusCode + ", Content:" + mContent + ", Cookie:" + mCookie + ", ErrorDetail:" + mErrorDetail;
	}

}



