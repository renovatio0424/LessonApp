package com.nhn.android.naverlogin.ui.view;


import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.data.OAuthLoginString;
import com.nhn.android.naverlogin.ui.OAuthLoginImage;
import com.nhn.android.naverlogin.util.DeviceDisplayInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OAuthLoginLayoutNaverAppDownloadBanner extends LinearLayout {

	private static final String TAG = OAuthLoginDefine.LOG_TAG + "OAuthLoginLayoutNaverAppDownloadBanner";

	
	
	private Context mContext;
	private float	mDensity;
	private int 	mDensityDpi;
	
	
	public OAuthLoginLayoutNaverAppDownloadBanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
		initView(attrs);
	}
	
	public OAuthLoginLayoutNaverAppDownloadBanner(Context context) {
		super(context);
		initData(context);
		initView(null);
	}

	private void initData(Context context) {
		mContext = context;
		mDensity = mContext.getResources().getDisplayMetrics().density;
		mDensityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
	}
	

	// 다른 layout 은 해상도별 dp 값 동일하나 font 의 경우 조금씩 다름 
	private float getTextSizeUpper() {
		if (DeviceDisplayInfo.isXhdpi(mDensityDpi)) {
			return 14f;
		} else if (DeviceDisplayInfo.isHdpi(mDensityDpi)) {
			return 13f;
		} else {
			return 12f;
		}
	}
	
	// 다른 layout 은 해상도별 dp 값 동일하나 font 의 경우 조금씩 다름
	private float getTextSizeUnder() {
		if (DeviceDisplayInfo.isXhdpi(mDensityDpi)) {
			return 12f;
		} else if (DeviceDisplayInfo.isHdpi(mDensityDpi)) {
			return 11f;
		} else {
			return 10.15f;
		}
	}
	
	private int pxFromDp(double dp) {
		int convPx = (int) (dp * mDensity);
		return convPx;
	}

	private void initView(AttributeSet attrs) {
		int width = LinearLayout.LayoutParams.FILL_PARENT;
		int height = LinearLayout.LayoutParams.WRAP_CONTENT;

		if (Build.VERSION.SDK_INT >= 8) {
			width = LinearLayout.LayoutParams.MATCH_PARENT;
		}
		
		setBackgroundColor(Color.rgb(254,252,227));
		setOrientation(HORIZONTAL);
		setLayoutParams(new LinearLayout.LayoutParams(width, height));
		
		addView(getNaverIconView());
		addView(getLayoutDownloadDesc());
		addView(getCloseBtnLayout());
	}
	
	// naver app download 
	private void downloadNaverApp() {
		Uri marketUri = Uri.parse("market://details?id=com.nhn.android.search");
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		((Activity)mContext).startActivity(marketIntent);
		((Activity)mContext).finish();
	}
	
	
	
	private ImageView getNaverIconView() {
		ImageView view = new ImageView(mContext);		
		Drawable drawableBgImage = OAuthLoginImage.getDrawableNaverIcon(getResources());
		
		int paddingDp = 10;
		int paddingPx = pxFromDp(paddingDp);

		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		view.setPadding(paddingPx, paddingPx, 0, paddingPx);
		view.setImageDrawable(drawableBgImage);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadNaverApp();
			}
		});
		
		return view;
	}
	
	// description area
	private LinearLayout getLayoutDownloadDesc() {
		LinearLayout layout = new LinearLayout(mContext);

		TextView tv1 = new TextView(mContext);		
		TextView tv2 = new TextView(mContext);
		
		int paddingDp = 10;
		int paddingPx = pxFromDp(paddingDp);
		
		layout.setPadding(paddingPx, paddingPx, 0, paddingPx);
		layout.setOrientation(VERTICAL);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setClickable(true);
		
		int padding4dp = pxFromDp(4);
		
		tv1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		tv1.setPadding(0, padding4dp, 0, padding4dp);
		tv1.setTextColor(Color.rgb(51,51,51)); // #333333
		
		tv1.setText(OAuthLoginString.naveroauthlogin_string_msg_naverapp_download_desc.getString(mContext));
		tv1.setTypeface(null, Typeface.BOLD);
		tv1.setTextSize(getTextSizeUpper());
		
		tv2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		tv2.setPadding(0, 0, 0, padding4dp);
		tv2.setText(OAuthLoginString.naveroauthlogin_string_msg_naverapp_download_link.getString(mContext));
		tv2.setTextColor(Color.rgb(45,180,0)); // #2db400
		tv2.setPaintFlags(tv2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tv2.setTextSize(getTextSizeUnder());
		
		layout.addView(tv1);
		layout.addView(tv2);
		
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				downloadNaverApp();
			}
		});
		
		return layout;
	}
	
	// close button area
	private RelativeLayout getCloseBtnLayout() {
		ImageView btnImgClose = new ImageView(mContext);
		Drawable drawableBgImage = OAuthLoginImage.getDrawableCloseImg(getResources());
		
		int paddingDp = 10;
		int paddingPx = pxFromDp(paddingDp);
		
		int paramMatchContent = LinearLayout.LayoutParams.FILL_PARENT;
		if (Build.VERSION.SDK_INT >= 8) {
			paramMatchContent = LinearLayout.LayoutParams.MATCH_PARENT;
		}
		
		btnImgClose.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, paramMatchContent));
		btnImgClose.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
		btnImgClose.setImageDrawable(drawableBgImage);
		btnImgClose.setScaleType(ScaleType.FIT_START);

		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) btnImgClose.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		
		btnImgClose.setLayoutParams(params);		
		btnImgClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisibility(GONE);
			}
		});

		RelativeLayout layout = new RelativeLayout(mContext);
		layout.setLayoutParams(new LinearLayout.LayoutParams(0, paramMatchContent, 1f));
		layout.addView(btnImgClose);
		
		return layout;
	}
}
