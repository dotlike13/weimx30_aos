package com.coinplug.wemixwallet.sdk;

import com.coinplug.wemixwallet.sdk.data.ResultData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Auth 서버 응답 객체
 * <p/>
 *
 * @author ybjeon
 */
public class A2AResponse{
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    @Expose
    private ResultData result;
    @SerializedName("expirationTime")
    @Expose
    private String expirationTime;

    private enum ResponseStatus{
        proposal,
        completed,
        canceled,
        expired
    }

    public String getRequestId(){
        return requestId;
    }


    public String getStatus(){
        return status;
    }


    public String getExpirationTime(){
        return expirationTime;
    }

    public ResultData getResult(){
        return result;
    }

    /**
     * 초기화 성공 여부
     *
     * @return 초기화 됐으면 <code>true</code> 반환
     */
    boolean isSuccess(){
        boolean statusResult = false;
        if(status.equals(ResponseStatus.proposal.toString()) || status.equals(ResponseStatus.completed.toString()) ){
            statusResult = true;
        }
        return statusResult;
    }


}
