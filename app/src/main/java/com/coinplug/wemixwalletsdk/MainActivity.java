package com.coinplug.wemixwalletsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coinplug.wemixwallet.sdk.ResultHandler;
import com.coinplug.wemixwallet.sdk.WemixWalletSDK;
import com.coinplug.wemixwallet.sdk.data.ExecuteContract;
import com.coinplug.wemixwallet.sdk.data.Metadata;
import com.coinplug.wemixwallet.sdk.data.SendNFT;
import com.coinplug.wemixwallet.sdk.data.SendToken;
import com.coinplug.wemixwallet.sdk.data.SendWemix;
import com.coinplug.wemixwalletsdk.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity{

    private WemixWalletSDK walletSdk = null;
    private ActivityMainBinding binding = null;
    private Metadata metadata = null;
    private String requestID = null;
    private final ResultHandler resultHandler = new ResultHandler(){
        @Override
        public void onAuthInitFailed(int statusCode){

        }

        @Override
        public void onNotInstall(final Intent intent){
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "Not install WemixWallet", Toast.LENGTH_SHORT).show();
            });
        }


        @Override
        public void onResult(int resultCode, String requestId){
            Toast.makeText(MainActivity.this, "requestId=" + requestId + " resultCode=" + resultCode, Toast.LENGTH_SHORT).show();

            if(resultCode == Activity.RESULT_OK){
                // request verify (state, code) => (isSuccess, user data)
                requestID = requestId;

            }
        }
    };

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

    private void initUI(){
        binding.radioGroup.check(R.id.radio1);
        binding.myAddress.setVisibility(View.GONE);
        binding.toAddress.setVisibility(View.GONE);
        binding.value1.setVisibility(View.GONE);
        binding.value2.setVisibility(View.GONE);
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch(i){
                case R.id.radio1:
                    binding.myAddress.setVisibility(View.GONE);
                    binding.toAddress.setVisibility(View.GONE);
                    binding.value1.setVisibility(View.GONE);
                    binding.value2.setVisibility(View.GONE);
                    break;
                case R.id.radio2:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.value);
                    binding.value2.setVisibility(View.GONE);
                    break;
                case R.id.radio3:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.value);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.contract);

                    break;
                case R.id.radio4:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.contract);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.tokenId);

                    break;
                case R.id.radio5:
                    binding.myAddress.setVisibility(View.VISIBLE);
                    binding.toAddress.setVisibility(View.VISIBLE);
                    binding.value1.setVisibility(View.VISIBLE);
                    binding.value1.setHint(R.string.abi);
                    binding.value2.setVisibility(View.VISIBLE);
                    binding.value2.setHint(R.string.params);
                    break;
                default:
                    break;

            }
        });

        binding.requestBtn.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view){
                String from;
                String to;
                String value1;
                String value2;
                from = binding.myAddress.toString();
                to = binding.toAddress.toString();
                value1 = binding.value1.toString();
                value2 = binding.value2.toString();
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

            }
        });

        binding.resultBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                walletSdk.getResult(requestID);
            }
        });
    }
}