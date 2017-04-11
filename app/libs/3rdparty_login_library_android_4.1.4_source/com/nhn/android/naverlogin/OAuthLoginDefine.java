package com.nhn.android.naverlogin;

public class OAuthLoginDefine {
	// 네아로 Lib 버젼
	public static final String 		VERSION = "4.1.4.1";		// for V App 
	// 로그 TAG 
	public static final String 		LOG_TAG ="NaverLoginOAuth|";
	// 네이버앱 package name
	public static final String 		NAVER_PACKAGE_NAME = "com.nhn.android.search";
	// Naver App 혹은 앱스토어앱 에서 받을 OAuth 2.0 로그인 Action
	public static final String 		ACTION_OAUTH_LOGIN 			= "com.nhn.android.search.action.OAUTH2_LOGIN";
	public static final String 		ACTION_OAUTH_LOGIN_2NDAPP 	= "com.naver.android.action.OAUTH2_LOGIN";


	/**
	 * 사용자가 수정 가능한 값들 
	 */
	// 네이버앱이 없거나 업그레이드가 필요한 경우 네이버앱에 대한 market link 팝업을 띄울것인지 여부 
	public static boolean		MARKET_LINK_WORKING = true;
	// network timeout
	public static int 			TIMEOUT = 10000;
	// 개발자 로그 출력 여부 설정 
	public static boolean 		DEVELOPER_VERSION = false;
	
}
