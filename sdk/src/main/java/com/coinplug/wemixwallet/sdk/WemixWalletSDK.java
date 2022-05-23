package com.coinplug.wemixwallet.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.coinplug.wemixwallet.sdk.data.ExecuteContract;
import com.coinplug.wemixwallet.sdk.data.Metadata;
import com.coinplug.wemixwallet.sdk.data.SendNFT;
import com.coinplug.wemixwallet.sdk.data.SendToken;
import com.coinplug.wemixwallet.sdk.data.SendWemix;
import com.coinplug.wemixwallet.util.Logger;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Wemixwallet 앱과 연동하는 SDK
 * <p/>
 *
 * @author ybjeon
 */
@SuppressWarnings("unused")
public class WemixWalletSDK{
    private static final String WEMIXWALLET_PACKAGE_NAME = "com.coinplug.wemixwallet";

    private static final int REQUEST_CODE_SIGN = 1001;

    private static final String LOG_TAG = WemixWalletSDK.class.getSimpleName();

    private enum RequestType{
        auth,
        send,
        send_token,
        send_nft,
        contract_execute
    }

    private final Gson gson = new Gson();

    static{
        // set tag, level of tag
        Logger.setDefaultTag(LOG_TAG);
        Logger.setLoggerLevel(Logger.Level.DEBUG);
    }

    private final ResultHandler resultHandler;

    private final ActivityWrapper activityWrapper;

    /**
     * SDK constructor for activity
     *
     * @param activity      요청 대상 activity
     * @param resultHandler 결과를 통보 받을 instance
     */
    public WemixWalletSDK(@NonNull Activity activity, @NonNull ResultHandler resultHandler){
        this(new ActivityWrapper(activity), resultHandler);
    }

    /**
     * SDK constructor for fragment
     *
     * @param fragment      요청 대상 fragment
     * @param resultHandler 결과를 받을 instance
     */
    public WemixWalletSDK(@NonNull Fragment fragment, @NonNull ResultHandler resultHandler){
        this(new ActivityWrapper(fragment), resultHandler);
    }

    /**
     * SDK constructor
     *
     * @param activityWrapper activity or fragment
     * @param resultHandler   결과를 받을 instance
     */
    private WemixWalletSDK(@NonNull ActivityWrapper activityWrapper, @NonNull ResultHandler resultHandler){
        this.resultHandler = resultHandler;
        this.activityWrapper = activityWrapper;

        // set service_id in manifest
        try{
            Context context = activityWrapper.getContext();
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        }catch(PackageManager.NameNotFoundException e){
            throw new RuntimeException("Not found Wemixwallet App.");
        }
    }

    /**
     * Check installed Wemixwallet App.
     *
     * @return if App is installed and enable, <code>true</code>
     */
    private boolean checkInstall(){
        // check install or enable
        try{
            ApplicationInfo info = activityWrapper.getContext().getPackageManager().getApplicationInfo(WEMIXWALLET_PACKAGE_NAME, 0);
            if(!info.enabled){
                Logger.warn("Not enabled package : " + WEMIXWALLET_PACKAGE_NAME);
                return false;
            }
        }catch(Exception e){
            // package not found
            Logger.debug("Not found package : " + WEMIXWALLET_PACKAGE_NAME);
            return false;
        }
        return true;
    }

    /**
     * @param metadata 요청 app data
     */
    public void auth(@NonNull Metadata metadata){
        A2AProposalRequest requestData = new A2AProposalRequest(metadata, RequestType.auth.toString(), null);
        requestProposalA2A(requestData);
    }

    /**
     * @param metadata  요청 app data
     * @param sendWemix transaction data
     */
    public void sendWemix(@NonNull Metadata metadata, SendWemix sendWemix){
        A2AProposalRequest requestData = new A2AProposalRequest(metadata,RequestType.send.name(),sendWemix.getTransactionData());
        requestProposalA2A(requestData);
    }

    /**
     * @param metadata  요청 app data
     * @param sendToken transaction data
     */
    public void sendToken(@NonNull Metadata metadata, SendToken sendToken){
        A2AProposalRequest requestData = new A2AProposalRequest(metadata,RequestType.send_token.name(),sendToken.getTransactionData());
        requestProposalA2A(requestData);
    }

    /**
     * @param metadata 요청 app data
     * @param sendNFT transaction data
     */
    public void sendNFT(@NonNull Metadata metadata, SendNFT sendNFT){
        A2AProposalRequest requestData = new A2AProposalRequest(metadata,RequestType.send_nft.name(),sendNFT.getTransactionData());
        requestProposalA2A(requestData);
    }

    /**
     * @param metadata        요청 app data
     * @param executeContract transaction data
     */
    public void executeContract(@NonNull Metadata metadata, ExecuteContract executeContract){
        A2AProposalRequest requestData = new A2AProposalRequest(metadata,RequestType.contract_execute.name(),executeContract.getTransactionData());
        requestProposalA2A(requestData);
    }

    private void requestProposalA2A(@NonNull A2AProposalRequest requestData){
        String body = gson.toJson(requestData);
        Logger.debug("requestProposalA2A body : " + body);
        // make launch url
        final Uri.Builder builder = Uri.parse("http://" + BuildConfig.A2A_SERVER_DOMAIN + "/api/v1/a2a/proposal").buildUpon();
        // call http
        new Thread(() -> {
            try{
                Logger.debug("requestProposalA2A launch : " + builder.build().toString());

                URL url = new URL(builder.build().toString());

                // launch from auth server
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoInput(true);
                //POST로 넘겨줄 파라미터 생성
                byte[] request_data = body.getBytes(StandardCharsets.UTF_8);
                OutputStream os = conn.getOutputStream();
                os.write(request_data);
                os.close();

                int status = conn.getResponseCode();
                Logger.debug("requestProposalA2A init response status : " + status);
                InputStream is;
                if(status < 400){
                    is = conn.getInputStream();
                }else{
                    is = conn.getErrorStream();
                }

                try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while((len = is.read(buffer, 0, buffer.length)) != -1){
                        baos.write(buffer, 0, len);
                    }
                    String stringBody = new String(baos.toByteArray(), StandardCharsets.UTF_8);

                    Logger.debug("requestProposalA2A  response body : " + stringBody);

                    JSONObject jsonObject = new JSONObject(stringBody);
                    final A2AProposalResponse response = A2AProposalResponse.fromJSONObect(jsonObject);
                    if(response.isSuccess()){
                        Logger.debug("requestProposalA2A  requestId  : " + response.getRequestId());
                        // check install or enable
                        if(checkInstall()){
                            resultHandler.onResult(status, response.getRequestId());
                            // Wemixwallet 앱 호출
//                            new Handler(Looper.getMainLooper()).post(() -> launch(response.getRequestId()));
                        }else{
                            // play store link.
                            resultHandler.onNotInstall(getIntent(RequestSchemeCreator.create(activityWrapper, response.getRequestId())));
                        }
                    }else{
                        // 요청 실패
                        Logger.debug("요청 실패" );
                        onRequestFailed(status);
                    }
                }catch(JSONException e){
                    Logger.debug("JSONException" );
                    // 서버에서 준 응답을 파싱 못합. 서버 또는 네트워크 에러 경우
                    onRequestFailed(status);
                }
            }catch(MalformedURLException e){
                // Never happened
                Logger.debug("IOException" );
            }catch(IOException e){
                Logger.debug("IOException" );
                onRequestFailed(-1);
            }
        }).start();
    }

    public void getResult(@NonNull String requestId){
        getProposalResponseA2A(requestId);
    }

    private void getProposalResponseA2A( String requestId){
        // make launch url
        final Uri.Builder builder = Uri.parse("http://" + BuildConfig.A2A_SERVER_DOMAIN + "/api/v1/a2a/result").buildUpon()
                .appendQueryParameter("requestId", requestId);
        // call http
        new Thread(() -> {
            try{
                Logger.debug("getProposalResponseA2A  : " + builder.build().toString());

                URL url = new URL(builder.build().toString());

                // launch from auth server
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");

                int status = conn.getResponseCode();
                Logger.debug("requestProposalA2A init response status : " + status);
                InputStream is;
                if(status < 400){
                    is = conn.getInputStream();
                }else{
                    is = conn.getErrorStream();
                }

                try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while((len = is.read(buffer, 0, buffer.length)) != -1){
                        baos.write(buffer, 0, len);
                    }
                    String stringBody = new String(baos.toByteArray(), StandardCharsets.UTF_8);

                    Logger.debug("requestProposalA2A init response body : " + stringBody);

                    JSONObject jsonObject = new JSONObject(stringBody);
                    final A2AProposalResponse response = A2AProposalResponse.fromJSONObect(jsonObject);
                    if(response.isSuccess()){

                        // check install or enable
                        if(checkInstall()){
                            // Wemixwallet 앱 호출
                            resultHandler.onResult(status, response.getRequestId());
//                            new Handler(Looper.getMainLooper()).post(() -> launch(response.getRequestId()));
                        }else{
                            // play store link.
                            resultHandler.onNotInstall(getIntent(RequestSchemeCreator.create(activityWrapper, response.getRequestId())));
                        }
                    }else{
                        // 요청 실패
                        onRequestFailed(status);
                    }
                }catch(JSONException e){
                    // 서버에서 준 응답을 파싱 못합. 서버 또는 네트워크 에러 경우
                    onRequestFailed(status);
                }
            }catch(MalformedURLException e){
                // Never happened
            }catch(IOException e){
                onRequestFailed(-1);
            }
        }).start();
    }



    /**
     * request 서버에서 실패
     *
     * @param statusCode http status code
     */
    private void onRequestFailed(final int statusCode){
        new Handler(Looper.getMainLooper()).post(() -> {
            Logger.warn("request failed : code=" + statusCode);
            resultHandler.onAuthInitFailed(statusCode);
        });
    }

    /**
     * Wemixwallet 앱을 인증 요청
     * @param requestId a2a response 의 requestId
     */
    private void launch(String requestId){
        // call Wemixwallet
        Intent intent = new Intent(Intent.ACTION_VIEW, RequestSchemeCreator.create(activityWrapper, requestId));
        intent.setPackage(WEMIXWALLET_PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        Logger.debug("Request to Wemixwallet : " + intent);

        try{
            activityWrapper.startActivityForResult(intent, REQUEST_CODE_SIGN);
        }catch(ActivityNotFoundException e){
            Logger.warn("Not start Wemixwallet", e);
        }
    }

    /**
     * Wemixwallet 에서 전달 준 결과를 처리한다.
     * <p/>
     * {@link Activity} 의 onActivityResult 에서 호출해줘야 한다.<p/>
     * <code>
     * protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     * if (sdk.handleResult(requestCode, resultCode, data)) {
     * return;
     * }
     * super.onActivityResult(requestCode, resultCode, data);
     * }
     * </code>
     *
     * @param requestCode request code on onActivityResult
     * @param resultCode  result code on onActivityResult
     * @param data        intent on onActivityResult
     * @return 처리 여부
     */
    public boolean handleResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_SIGN){
            Logger.debug("Response from Wemixwallet : resultCode=" + resultCode);
            if(data != null){
                String typeStr = data.getStringExtra("type");
                int type = -1;
                if(typeStr != null){
                    type = Integer.parseInt(typeStr);
                }
                resultHandler.onResult(resultCode, data.getStringExtra("state"));
            }else{
                Logger.error("Data of response from Wemixwallet is null");
                resultHandler.onResult(resultCode, null);
            }
            return true;
        }
        return false;
    }


    /**
     * Get an intent to install Wemixwallet app.
     *
     * @return intent
     */
    private static Intent getIntent(Uri uri){
        String query = null;
        if(uri != null){
            try{
                query = "?id=" + WEMIXWALLET_PACKAGE_NAME + "&url=" + URLEncoder.encode(uri.toString(), "utf-8");
            }catch(UnsupportedEncodingException e){
                return new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details"));
            }
        }

        try{
            return new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details" + query));
        }catch(Exception e){
            // web browser
            return new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details" + query));
        }
    }
}
