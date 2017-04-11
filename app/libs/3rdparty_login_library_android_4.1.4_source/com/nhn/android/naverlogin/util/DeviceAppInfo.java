package com.nhn.android.naverlogin.util;

import java.util.List;
import java.util.Locale;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

// 기기와 앱정보를 알려주는 클래스
/*
 * useragent 값이나, 기기 고유번호등을 생성해주고 기기의 ip나 언어등을 확인해줌
 * @author naver
 *
 */
public class DeviceAppInfo {
	private static final String TAG = OAuthLoginDefine.LOG_TAG + "DeviceAppInfo";

	// Singleton
	private static final DeviceAppInfo mInstance = new DeviceAppInfo();

	public static DeviceAppInfo getBaseInstance() {
		return mInstance;	
	}
	private DeviceAppInfo() {
	}

	/**
	 * 현재 단말기의 언어 설정을 확인하여 한글인지 여부를 리턴함 
	 * @param context context
	 * @return korean 여부 
	 */
	public boolean isKorean(Context context) {
		Locale systemLocale = context.getResources().getConfiguration().locale;
		String strLanguage = systemLocale.getLanguage();
		if (strLanguage.startsWith("ko"))
			return true;
		return false;
	}

	public String getUserAgent(Context context) {
		String androidVersionInfo = "Android/" + (android.os.Build.VERSION.RELEASE);
		String modelInfo = "Model/" + (android.os.Build.MODEL);
		
		String useragent = androidVersionInfo.replaceAll("\\s", "") + " " + modelInfo.replaceAll("\\s", "");
		String packageName = "";
		
		try {		
			PackageManager pm = context.getPackageManager();
			packageName = context.getPackageName();
			PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_GIDS | PackageManager.GET_SIGNATURES | PackageManager.GET_META_DATA);
			
			String appId = "";
			if (info.applicationInfo.loadDescription(pm) != null) {
				appId = ",appId:" + info.applicationInfo.loadDescription(pm);
			}
			String appInfo = String.format("%s/%s(%d,uid:%d%s)", packageName, info.versionName, info.versionCode, info.applicationInfo.uid, appId);
			String loginModuleInfo = "OAuthLoginMod/" + OAuthLogin.getVersion();

			useragent += " " + appInfo.replaceAll("\\s", "") + " " + loginModuleInfo.replaceAll("\\s", "");
 		} catch (NameNotFoundException e) {
 			Log.e(TAG, "not installed app : " + packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return useragent;
	}

	public String getLocaleString(Context context) {
		String strLanguage = "ko_KR"; 
		try {
			Locale systemLocale = context.getResources().getConfiguration().locale;
			strLanguage = systemLocale.toString();
			
			if (TextUtils.isEmpty(strLanguage)) {
				return "ko_KR";
			} 
		} catch (Exception e) {
			// do nothing
		}
		
		return strLanguage;
	}
		

	/**
	 * 현재 설치되어 있는 패키지가 있는 확인  
	 * @param context context
	 * @param packageName 패키지명 (예: "com.nhn.android.search") 
	 * @return if 네이버앱 있음, true. else, false
	 */
	public static boolean isAppExist(Context context, String packageName) {
		try {
			if (TextUtils.isEmpty(packageName)) {
				return false;
			}
			
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);

			// 네이버앱 미설치
			if (intent == null) {
				Log.i(TAG, packageName + " is not installed.");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true; 
	}
	
	
	public static boolean isIntentFilterExist(Context context, String intentName) {
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(new Intent(intentName), PackageManager.GET_META_DATA);
		for (ResolveInfo resolveInfo : list) {
			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "intent filter name:" + intentName);
				Log.d(TAG, "package name:" + resolveInfo.activityInfo.packageName + ", " + resolveInfo.activityInfo.name);
			}
			return true;
		}
		return false;
	}
	

	/**
	 * 네이버앱을 제외한 앱들 중 oauth2 master 앱인 것 중 appstore 가 있으면 appstore 를 리턴하고 없으면 나머지 중 하나를 리턴한다.  
	 * @param context activity context 
	 * @return oauth2 master 앱 중 하나의 package name
	 */
	public static String getPrimaryNaverOAuth2ndAppPackageName(Context context) {
		try {
			// TODO task permission 설정 안한 경우 동작 어떻게 할지 고려하기 
			String[] appList = {"com.nhn.android.appstore"};
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(
												new Intent(OAuthLoginDefine.ACTION_OAUTH_LOGIN_2NDAPP), 
												PackageManager.GET_META_DATA);
	
			for (String appName : appList) {
				for (ResolveInfo resolveInfo : list) {
					if (OAuthLoginDefine.DEVELOPER_VERSION) {
						Log.d(TAG, "package name:" + resolveInfo.activityInfo.packageName + ", " + resolveInfo.activityInfo.name);
					}
					if (resolveInfo.activityInfo.packageName.equals(appName)) {
						return resolveInfo.activityInfo.packageName;
					}
				}
			}
		
			if (OAuthLoginDefine.DEVELOPER_VERSION) {
				Log.d(TAG, "no app assinged in order-list. package name:" + list.get(0).activityInfo.packageName + ", " + list.get(0).activityInfo.name);
			}

			// TODO phishing 의 우려가 있으므로 제거 추후 서버에서 app list 받아올 수 있도록 개발하기  
			//return list.get(0).activityInfo.packageName;
			
		} catch (Exception e) {
			
		}
		
		return null;
	}
	

	/**
	 * 앱 이름 알려줌
	 * @param context activity context
	 * @return 앱 label 명
	 */
	public static String getApplicationName(Context context) {
	    PackageManager lPackageManager = context.getPackageManager();
	    ApplicationInfo lApplicationInfo;
	    String appName = "NAVER";
	    
		try {
			lApplicationInfo = lPackageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
			appName = (String)lPackageManager.getApplicationLabel(lApplicationInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	    return appName;
	}

	/**
	 * get package name of this app
	 * @param context
	 * @return packagename
	 */
	public static String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}
}
