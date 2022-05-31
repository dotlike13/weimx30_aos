package com.coinplug.wemixwalletsdk;

import static com.coinplug.wemixwallet.sdk.WemixWalletSDK.REQUEST_CODE_PROPOSAL;
import static com.coinplug.wemixwallet.sdk.WemixWalletSDK.REQUEST_CODE_RESULT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coinplug.wemixwallet.sdk.A2AResponse;
import com.coinplug.wemixwallet.sdk.ResultHandler;
import com.coinplug.wemixwallet.sdk.WemixWalletSDK;
import com.coinplug.wemixwallet.sdk.data.ExecuteContract;
import com.coinplug.wemixwallet.sdk.data.Metadata;
import com.coinplug.wemixwallet.sdk.data.SendNFT;
import com.coinplug.wemixwallet.sdk.data.SendToken;
import com.coinplug.wemixwallet.sdk.data.SendWemix;
import com.coinplug.wemixwalletsdk.databinding.ActivityMainBinding;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity{

    private final String TAG = "dApp";
    private WemixWalletSDK walletSdk = null;
    private ActivityMainBinding binding = null;
    private Metadata metadata = null;
    private String requestIDResult = null;
    private String myAddress = null;
    private final ResultHandler resultHandler = new ResultHandler(){
        @Override
        public void onAuthInitFailed(int statusCode){

        }

        @Override
        public void onNotInstall(final Intent intent){
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Not install WemixWallet", Toast.LENGTH_SHORT).show());
        }


        /**
         * sdk 에서 받은 응답 처러
         * @param resultCode 결과 코드. 성공: {@link android.app.Activity#RESULT_OK}, 사용자취소: {@link android.app.Activity#RESULT_CANCELED}, 파라미터오류: 4,
         * @param requestId  requestId
         * @param response A2AResponse A2A서버에서 받는 응답
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onResult(int resultCode, String requestId, A2AResponse response){
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "requestId=" + requestId + " resultCode=" + resultCode, Toast.LENGTH_SHORT).show());
            Log.e(TAG,"resultCode = "+resultCode);
            if(resultCode == Activity.RESULT_OK){
                walletSdk.getResult(requestId);
            }else if(resultCode == REQUEST_CODE_PROPOSAL){
                requestIDResult = requestId;
            }else if(resultCode ==  REQUEST_CODE_RESULT){
                Log.e(TAG,"resultCode = "+  response.getStatus());
                Gson gson = new Gson();
                String res = gson.toJson(response);
                String address = response.getResult().getAddress();
                runOnUiThread(() ->{
                    binding.status.setText(response.getStatus());
                    binding.myAddress.setText(address);
                    myAddress = address;
                    if(response.getResult().getAddress() != null){
                        binding.data.setText(getString(R.string.address)+response.getResult().getAddress());
                    }else{
                        binding.data.setText(getString(R.string.txhash)+response.getResult().getTransactionHash());
                    }
                });
                Log.e(TAG,"response = "+res);
                requestIDResult = response.getRequestId();
            }else if(resultCode == Activity.RESULT_CANCELED){
                Log.e(TAG,"CANCEL");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (walletSdk.handleResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        walletSdk = new WemixWalletSDK(this, resultHandler);
        metadata = new Metadata("Test앱", "설명설명", null, null, null, null);
        initUI();

    }

    @SuppressLint("NonConstantResourceId")
    private void initUI(){
        Log.e(TAG,"initUI ");
        binding.radioGroup.check(R.id.radio1);
        binding.myAddress.setVisibility(View.GONE);
        binding.toAddress.setVisibility(View.GONE);
        binding.value1.setVisibility(View.GONE);
        binding.value2.setVisibility(View.GONE);
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i !=  R.id.radio1){
                binding.myAddress.setText(myAddress);
            }
            binding.status.setText(getString(R.string.status));

            switch(i){
                case R.id.radio1:
                    binding.myAddress.setVisibility(View.GONE);
                    binding.toAddress.setVisibility(View.GONE);
                    binding.value1.setVisibility(View.GONE);
                    binding.value2.setVisibility(View.GONE);
                    binding.data.setText(getString(R.string.address));
                    binding.requestBtn.setText(R.string.auth);
                    break;
                case R.id.radio2:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.value);
                    binding.value2.setVisibility(View.GONE);
                    binding.data.setText(getString(R.string.txhash));
                    binding.requestBtn.setText(R.string.send);
                    break;
                case R.id.radio3:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.value);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.contract);
                    binding.data.setText(getString(R.string.txhash));
                    binding.requestBtn.setText(R.string.sendToken);

                    break;
                case R.id.radio4:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.contract);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.tokenId);
                    binding.data.setText(getString(R.string.txhash));
                    binding.requestBtn.setText(R.string.sendNFT);
                    break;
                case R.id.radio5:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.abi);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.params);
                    binding.data.setText(getString(R.string.txhash));
                    binding.requestBtn.setText(R.string.contractExecute);
                    break;
                default:
                    break;

            }
        });

        binding.requestBtn.setOnClickListener(view -> {
            String from;
            String to;
            String value1;
            String value2;
            from = binding.myAddress.getText().toString();
            to = binding.toAddress.getText().toString();
            value1 = binding.value1.getText().toString();
            value2 = binding.value2.getText().toString();
            if(from.equals("")){
               from = "0x8D433bf803209977e54b07F3ED54f80EA38AeaE0";
            }
            if(to.equals("")){
                to = "0xcad9042Cf49684939A2F42c2d916d1B6526635c2";
            }
            if(value1.equals("")){
                value1 = "value";
            }
            if(value2.equals("")){
                value2 = "value2";
            }
            int i = binding.radioGroup.getCheckedRadioButtonId();
            switch(i){
                case R.id.radio1:
                    walletSdk.auth(metadata);
                    break;
                case R.id.radio2:
                    SendWemix sendWemix = new SendWemix(from, to, value1);
                    walletSdk.sendWemix(metadata, sendWemix);
                    break;
                case R.id.radio3:
                    SendToken sendToken = new SendToken(from, to, value1, value2);
                    walletSdk.sendToken(metadata, sendToken);
                    break;
                case R.id.radio4:
                    SendNFT sendNFT = new SendNFT(from, to, value1, value2);
                    walletSdk.sendNFT(metadata, sendNFT);
                    break;
                case R.id.radio5:
                    ExecuteContract executeContract = new ExecuteContract(from, to, value1, value2);
                    walletSdk.executeContract(metadata, executeContract);
                    break;
                default:
                    break;

            }

        });

//        binding.resultBtn.setOnClickListener(view -> {
//            Log.e(TAG,"resultCode = "+requestIDResult);
//            if(requestIDResult == null){
//                runOnUiThread(() -> Toast.makeText(MainActivity.this, "먼저 요청을 하세요", Toast.LENGTH_SHORT).show());
//            }else{
//                walletSdk.getResult(requestIDResult);
//            }
//        });
    }
}