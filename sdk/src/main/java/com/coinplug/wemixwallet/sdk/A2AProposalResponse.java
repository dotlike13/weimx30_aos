package com.coinplug.wemixwallet.sdk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Auth 서버 응답 객체
 * <p/>
 *
 * @author ybjeon
 */
class A2AProposalResponse{
    private String requestId;
    private String status;
    private String expirationTime;

    private enum ResponseStatus{
        proposal,
        completed,
        canceled,
        expired
    }

    private A2AProposalResponse(){
    }

    /**
     * JSON object 로 부터 응답객체를 생성한다.
     * <p/>
     *
     * @param jsonObject auth 서버에서 받은 응답 json
     * @return response instance
     * @throws JSONException json parsing error
     */
    static A2AProposalResponse fromJSONObect(JSONObject jsonObject) throws JSONException{
        A2AProposalResponse response = new A2AProposalResponse();
        response.status = jsonObject.getString("status");
        response.requestId = jsonObject.getString("requestId");
        response.expirationTime = jsonObject.getString("expirationTime");
        return response;
    }

    public String getRequestId(){
        return requestId;
    }

    public void setRequestId(String requestId){
        this.requestId = requestId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getExpirationTime(){
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime){
        this.expirationTime = expirationTime;
    }

    /**
     * 초기화 성공 여부
     *
     * @return 초기화 됐으면 <code>true</code> 반환
     */
    boolean isSuccess(){
        return status.equals(ResponseStatus.proposal.toString());
    }


}
