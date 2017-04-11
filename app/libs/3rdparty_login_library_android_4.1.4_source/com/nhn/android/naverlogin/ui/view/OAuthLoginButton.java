package com.nhn.android.naverlogin.ui.view;



import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.OAuthLoginImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * imagebutton for "Sign-in with NAVER"
 * @author nhn
 *
 */
public class OAuthLoginButton extends ImageButton {

	private Context mContext;
	private static OAuthLoginHandler mOAuthLoginHandler;
	
	// background image resource id
	private int		mBgDrawableResId = -1;
	
	public OAuthLoginButton(Context context) {
		super(context);
		init(context);
	}

	public OAuthLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OAuthLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 버튼을 클릭하여 로그인하는 경우, 로그인 결과를 받을 handler 를 지정해준다.
	 * @param oauthLoginHandler 로그인 결과를 받을 handler 
	 */
	public void setOAuthLoginHandler(final OAuthLoginHandler oauthLoginHandler) {
		mOAuthLoginHandler = oauthLoginHandler;
	}
	
	/**
	 * 네이버로 로그인 버튼의 색깔과 형태는 하나로 고정되어 표시됩니다.
	 * 다른 이미지를 사용하고 싶으시다면 setBgResourceName() 메쏘드를 사용해주세요.  
	 * set color and type of Naver Sign-in Button.
	 * @deprecated Use setBgResourceId(). this method is deprecated.(cause, containg image resource in jar make that file size is large.)  
	 * @param color unused
	 * @param type unused
	 */
	@Deprecated
	public void setBgType(String color, String type) {
		setBgType();
	}
	
	/**
	 * 버튼의 배경 이미지를  정해준다.
	 * 정해주지 않는 경우 디폴트 이미지가 표시됨
	 * 사용가능한 버튼 이미지는 네이버 아이디로 로그인 개발자 페이지의 
	 * BI 가이드(http://developer.naver.com/wiki/pages/NaverLoginBIGuide)에서 참고 가능 
	 * @param resId 버튼 배경 이미지로 쓸 resource id
	 */
	public void setBgResourceId(int resId) {
		mBgDrawableResId = resId;
	}
	
	private void setBgType() {
		
		if (-1 != mBgDrawableResId) {
			Drawable drawableBgImage = getResources().getDrawable(mBgDrawableResId);
			if (Build.VERSION.SDK_INT >= 16) {
		    	setBackground(drawableBgImage);
		    } else {
		    	setBackgroundDrawable(drawableBgImage);
		    }
			return ;
		}
		
		byte[] drawableByteBackGroundImg = null;

		drawableByteBackGroundImg = OAuthLoginImage.hexToByteArray(OAuthLoginImage.getDrawableByteStrLoginBtnImg());
		Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(drawableByteBackGroundImg, 0, drawableByteBackGroundImg.length);
        } catch (Exception e) {
            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            bitmap = BitmapFactory.decodeByteArray(drawableByteBackGroundImg, 0, drawableByteBackGroundImg.length, options);
        }

        Drawable bt = null;

		int newHeight = 50;
		int newWidth = 0;
		try {
			newHeight = getLayoutParams().height;
			newWidth = 0;
			if (0 < newHeight) {
				newWidth = (int)((double) newHeight / bitmap.getHeight() * bitmap.getWidth());
			} else {
				newWidth = getLayoutParams().width;
				if (0 < newWidth) {
					newHeight = (int)((double) newWidth / bitmap.getWidth() * bitmap.getHeight());
				} else {
					newHeight = 80;
					newWidth = (int)((double) newHeight / bitmap.getHeight() * bitmap.getWidth());
				}
			}
	
			bt = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true));
		} catch (Exception e) {
			bt = new BitmapDrawable(getResources(), bitmap);
			e.printStackTrace();
		}

		try {
			if (Build.VERSION.SDK_INT >= 16) {
		    	setBackground(bt);
		    } else {
		    	setBackgroundDrawable(bt);
		    }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	private void init(Context context) {
		mBgDrawableResId = -1;
		mContext = context;
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OAuthLogin.getInstance().startOauthLoginActivity((Activity)mContext, mOAuthLoginHandler);
			}
		});
	}
	

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (isInEditMode()) return ;

		setBgType();
		super.onLayout(changed, left, top, right, bottom);
	}
	
}
