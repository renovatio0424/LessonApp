package com.nhn.android.naverlogin.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

// progress dialog 관리해주는 클래스 
/*
 * @author naver
 * 
 */
public class OAuthLoginDialogMng {
	public Object 							mProgressDialogSync = new Object();
	public Object 							mAlertDialogSync = new Object();
	private ProgressDialog 					mNLoginGlobalDefaultProgressDialog = null;
	
	/**
	 * progress dialog를 보여줌
	 * 
	 * @param context context
	 * @param msg
	 *            dialog에 출력할 메시지
	 * @param onCancelListener
	 *            back-key 등으로 cancel 될 경우 실행될 listener. 주로 백그라운드로 처리되던 작업의 중지를 하는 로직이 들어감
	 * @return 생성실패하는 경우 false 리턴, 정상적인 경우 true 리턴
	 */
	public boolean showProgressDlg(Context context, String msg, DialogInterface.OnCancelListener onCancelListener) {

		synchronized(mProgressDialogSync) {
			try {
				if (mNLoginGlobalDefaultProgressDialog != null) {
					mNLoginGlobalDefaultProgressDialog.hide();
					mNLoginGlobalDefaultProgressDialog.dismiss();
				}
				mNLoginGlobalDefaultProgressDialog = new ProgressDialog(context);
				mNLoginGlobalDefaultProgressDialog.setIndeterminate(true);
				mNLoginGlobalDefaultProgressDialog.setMessage(msg);
				mNLoginGlobalDefaultProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				
				if (onCancelListener != null) {
					mNLoginGlobalDefaultProgressDialog.setOnCancelListener(onCancelListener);
				}
				mNLoginGlobalDefaultProgressDialog.setCanceledOnTouchOutside(false);
				mNLoginGlobalDefaultProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								// dismiss 되는 경우 null로 처리하여 다음 show Progress 하는데 문제
								// 없도록 함
								mNLoginGlobalDefaultProgressDialog = null;
							}
						});

				mNLoginGlobalDefaultProgressDialog.show();

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * showPregressDlg()로 만든 progress dialog를 없앰
	 * 
	 * @return 없거나 실패한경우 false 리턴, 정상적으로 없어진 경우 true 리턴
	 */
	public synchronized boolean hideProgressDlg() {
		synchronized(mProgressDialogSync) {
			if (mNLoginGlobalDefaultProgressDialog == null) {
				return false;
			}
			try {
				mNLoginGlobalDefaultProgressDialog.hide();
				mNLoginGlobalDefaultProgressDialog.dismiss();
				mNLoginGlobalDefaultProgressDialog = null;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}


}
