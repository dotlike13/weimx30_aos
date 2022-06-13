# WemixWalletSDK for Android
WemixWallet App으로 Auth(주소 요청), Wemix coin / token 보내기 / NFT 보내기 / 컨트랙 요청에 대한 기능을 Android에 제공합니다.

## WorkFlow

## 사용방법
* [주소 요청](#auth)
* [코인 보내기 요청](#send-wemix)
* [토큰 보내기 요청](#send-token)
* [NFT 보내기 요청](#send-nft)
* [컨트랙 실행 요청](#contract-execute)
* [요청 결과 확인](#result)


### Auth

지갑 주소를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    walletSdk.auth(metadata);
```

### Send Wemix
코인 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendWemix sendWemix = new SendWemix(from, to, amount);
    walletSdk.sendWemix(metadata, sendWemix);
```

### Send Token
토큰 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendToken sendToken = new SendToken(from, to, value, contract);
    walletSdk.sendToken(metadata, sendToken);
```

### Send NFT
NFT 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendNFT sendNFT = new SendNFT(from, to, contract, tokenId);
    walletSdk.sendNFT(metadata, sendNFT);
```

### Contract Execute
컨트랙을 요청합니다.

```java
     WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
     //appname not null
     Metadata metadata = new Metadata("app name", "description", null, null, null, null);
     ExecuteContract executeContract = new ExecuteContract(from, to, abi, parmas);
     walletSdk.executeContract(metadata, executeContract);
```

### Result
요청 응답값을 처리 합니다.
```java

    //requestId는 위에서 요청한 응답값입니다.
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
             * @param resultCode 결과 코드. 성공: {@link android.app.Activity#RESULT_OK}, 사용자취소: {@link android.app.Activity#RESULT_CANCELED}
             * @param requestId requestId
             * @param response A2AResponse A2A서버에서 받는 응답
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onResult(int resultCode, String requestId, A2AResponse response){
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "requestId=" + requestId + " resultCode=" + resultCode, Toast.LENGTH_SHORT).show());
                Log.e(TAG,"resultCode = "+resultCode);
                //인증요청
                if(resultCode == Activity.RESULT_OK){
                    walletSdk.getResult(requestId);
                }
                //코인보내기, 토큰보내기, NFT보내기, 컨트랙실행 요청
                else if(resultCode == REQUEST_CODE_PROPOSAL){
                    requestIDResult = requestId;
                }
                //결과확인
                else if(resultCode ==  REQUEST_CODE_RESULT){
                    Log.e(TAG,"resultCode = "+  response.getStatus());
                    Gson gson = new Gson();
                    //결과 요청 응답값
                    String res = gson.toJson(response);
                    Log.e(TAG,"response = "+res);
                    requestIDResult = response.getRequestId();
                }else if(resultCode == Activity.RESULT_CANCELED){
                    //취소 요청 처리
                    Log.e(TAG,"CANCEL");
                }
            }
        };
```

## Requirements
AndroidManifest.xml 파일에 호출하는 앱 package명를 추가해야 합니다.
```xml
    <queries>
        <package android:name="com.coinplug.wemixwallet" />
    </queries>
 ```

## Author

bslee, bslee@coinplug.com

## License

WemixWalletSDK-Android is available under the MIT license. See the LICENSE file for more info.
