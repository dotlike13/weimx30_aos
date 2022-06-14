# WemixWalletSDK for Android
WemixWallet App으로 Auth(주소 요청), Wemix coin / token 보내기 / NFT 보내기 / 컨트랙 요청에 대한 기능을 Android에 제공합니다.

## WorkFlow

## 사용방법
* [작업요청 결과 확인](#proposal_result)
* [결과 확인](#response_result)
* [주소 요청](#auth)
* [코인 보내기 요청](#send-wemix)
* [토큰 보내기 요청](#send-token)
* [NFT 보내기 요청](#send-nft)
* [컨트랙 실행 요청](#contract-execute)


### Proposal Result
작업 요청 응답 값을 처리

```java
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
                //작업 실행 결과 요청
                walletSdk.getResult(requestId,responseResultHandler);
            }else if(resultCode == Activity.RESULT_CANCELED){
                Log.e(TAG, "CANCEL");
            }
        }
    };
```

### Response Result
요청 결과 값을 처리

```java
    //작업 실행 결과 확인
    private final ResponseResultHandler responseResultHandler = new ResponseResultHandler(){
        @Override
        public void onResult(String requestId, A2AResponse response){
            Log.e(TAG, "resultCode = " + response.getStatus());
            Gson gson = new Gson();
            //작업 요청 실행 결과값
            String res = gson.toJson(response);
            Log.e(TAG, "response = " + res);
        }
    };
```

### Auth

지갑 주소를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    walletSdk.proposal(metadata, null);
```

### Send Wemix
코인 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendWemix sendWemix = new SendWemix(from, to, amount);
    walletSdk.proposal(metadata, sendWemix);
```

### Send Token
토큰 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendToken sendToken = new SendToken(from, to, value, contract);
    walletSdk.proposal(metadata, sendToken);
```

### Send NFT
NFT 보내기를 요청합니다.

```java
    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
    //appname not null
    Metadata metadata = new Metadata("app name", "description", null, null, null, null);
    SendNFT sendNFT = new SendNFT(from, to, contract, tokenId);
    walletSdk.proposal(metadata, sendNFT);
```

### Contract Execute
컨트랙을 요청합니다.

```java
     WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
     //appname not null
     Metadata metadata = new Metadata("app name", "description", null, null, null, null);
     ExecuteContract executeContract = new ExecuteContract(from, to, abi, parmas);
     walletSdk.proposal(metadata, executeContract);
```

### Result
요청 응답값을 처리 합니다.


## Requirements
AndroidManifest.xml 파일에 호출하는 앱 package명를 추가해야 합니다.
```xml
    <queries>
        <package android:name="com.wemixfoundation.wemixwallet" />
    </queries>
 ```

## Author

bslee, bslee@coinplug.com

## License

WemixWalletSDK-Android is available under the MIT license. See the LICENSE file for more info.
