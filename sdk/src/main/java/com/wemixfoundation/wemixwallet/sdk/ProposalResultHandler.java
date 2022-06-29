package com.wemixfoundation.wemixwallet.sdk;

import android.content.Intent;

/**
 * 결과를 전달 받을 interface<p/>
 *
 * @author ybjeon
 */
public interface ProposalResultHandler{
    /**
     * 인증 초기화 실패
     * <p/>
     * Auth 서버와 인증 초기화 시 실패한 경우에 호출됨
     *
     * @param statusCode Http status code
     */
    void onAuthInitFailed(int statusCode);


    /**
     * Wemixwallet 앱에서 인증요청에 대한 결과가 왔을 때 호출
     *
     * @param resultCode 결과 코드. 성공: {@link android.app.Activity#RESULT_OK}, 사용자취소: {@link android.app.Activity#RESULT_CANCELED}, 파라미터오류: 4,
     * @param requestId  requestId
     */
    void onProposalResult(int resultCode, String requestId);

    /**
     * Wemixwallet 이 설치 되어 있지 않는 경우 호출
     * <p/>
     *
     * @param intent PlayStore 로 이동시키는 intent
     */
    void onNotInstall(Intent intent);
}
