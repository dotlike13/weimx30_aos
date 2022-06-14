package com.coinplug.wemixwalletsdk;

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
import com.coinplug.wemixwallet.sdk.ProposalResultHandler;
import com.coinplug.wemixwallet.sdk.ResponseResultHandler;
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
    private String myAddress = null;
    //작업 요청 결과 확인
    private final ProposalResultHandler resultHandler = new ProposalResultHandler(){
        @Override
        public void onAuthInitFailed(int statusCode){
            //네트워크 에러 처리
        }

        @Override
        public void onNotInstall(final Intent intent){
            //앱 미설치 에러 처리
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Not install WemixWallet", Toast.LENGTH_SHORT).show());
        }

        //작업요청 응답
        @Override
        public void onProposalResult(int resultCode, String requestId){
            Log.e(TAG, "onAuthResult = " + requestId);
            if(resultCode == Activity.RESULT_OK){
                walletSdk.getResult(requestId, responseResultHandler);
            }else if(resultCode == Activity.RESULT_CANCELED){
                Log.e(TAG, "CANCEL");
            }
        }
    };

    //작업 요청 시행 결과 확인
    private final ResponseResultHandler responseResultHandler = new ResponseResultHandler(){
        @Override
        public void onResult(String requestId, A2AResponse response){
            Log.e(TAG, "resultCode = " + response.getStatus());
            Gson gson = new Gson();
            String res = gson.toJson(response);
            String address = null;
            if(response.getResult().getAddress() != null){
                myAddress = response.getResult().getAddress();
                runOnUiThread(() -> {
                    binding.myAddress.setText(myAddress);
                });
            }
            runOnUiThread(() -> {
                binding.status.setText(response.getStatus());
                if(response.getResult().getAddress() != null){
                    binding.data.setText(getString(R.string.address) + response.getResult().getAddress());
                }else{
                    binding.data.setText(getString(R.string.txhash) + response.getResult().getTransactionHash());
                }
            });
            Log.e(TAG, "response = " + res);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        Log.e(TAG, "onActivityResult");
        if(walletSdk.handleResult(requestCode, resultCode, data)){
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
        Log.e(TAG, "initUI ");
        binding.radioGroup.check(R.id.radio1);
        binding.myAddress.setVisibility(View.GONE);
        binding.toAddress.setVisibility(View.GONE);
        binding.value1.setVisibility(View.GONE);
        binding.value2.setVisibility(View.GONE);
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i != R.id.radio1){
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
                    walletSdk.proposal(metadata, null);
                    break;
                case R.id.radio2:
                    SendWemix sendWemix = new SendWemix(from, to, value1);
                    walletSdk.proposal(metadata, sendWemix);
                    break;
                case R.id.radio3:
                    SendToken sendToken = new SendToken(from, to, value1, value2);
                    walletSdk.proposal(metadata, sendToken);
                    break;
                case R.id.radio4:
                    SendNFT sendNFT = new SendNFT(from, to, value1, value2);
                    walletSdk.proposal(metadata, sendNFT);
                    break;
                case R.id.radio5:
                    ExecuteContract executeContract = new ExecuteContract(from, to, value1, value2);
                    walletSdk.proposal(metadata, executeContract);
                    break;
                default:
                    break;

            }

        });

    }
}