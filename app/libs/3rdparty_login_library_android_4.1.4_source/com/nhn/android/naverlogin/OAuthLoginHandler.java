package com.nhn.android.naverlogin;
import android.os.Handler;
import android.os.Message;


/**
 * 네아로 SDK를 이용한 OAuth2.0 연동 과정이 종료되는 걸 알 수 있는 handler
 * - OAuthLoginHandler 의 run() 메쏘드를 구현해서 아래 2개의 method의 파라미터로 넘겨주면 연동이 종료되었음을 알 수 있다. 
 * - OAuthLogin.startOAuthLoginActivity() 호출시 파라미터로 넘겨줌
 * - OAuthLoginButton.setOAuthLoginHandler() 호출시 파라미터로 넘겨줌
 * @author naver
 *
 */
public abstract class OAuthLoginHandler extends Handler {

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		boolean success = (msg.what == 1);
		run(success);
	}
	
	/**
	 * if success == true, OAuth2.0 인증이 성공하여 access token 과 refresh token 을 정상적으로 발급 받은 경우.
	 * if success == false, 인증이 실패하거나, 다른 이유로 access token 및 refresh token 을 정상적으로 발급 받지 못한 경우.
	 * 연동 결과로 얻은 데이터는 getAccessToken() / getRefreshToken() / getLastErrorCode() 등의 메소드를 통해 알 수 있다.  
	 * @param success 성공 여부 (상세 설명은 위 참고)
	 */
	public abstract void run(boolean success);
}
