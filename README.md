# Android SDK
This page describes the Android SDK for interworking with WEMIX Wallet in dApps.


## Requirements
* API 23 : Android 6.0(Marshmallow) or above

## Settings
It is possible to operate in an environment where HTTP(s) communication is basically possible without a separate subscription procedure.

### Adding dependency with Gradle
Add the following to your build.gradle file:

```gradle
    dependencies {
        implementation 'com.github.wemixarchive:wemix-wallet-sdk-android:0.8.0'
    }
```

### Adding Permission
Permission to Internet communication is required in order to use the SDK. Add Permission to the AndroidManifest.xml file as shown below.
```xml
 <uses-permission android:name="android.permission.INTERNET" />
```

### Set Server address
You can set the api server address in the AndroidManifest.xml file.
If not set, it will be set as default.
```xml
<meta-data android:name="A2A_SERVER_DOMAIN" android:value="a2a.test.wemix.com" />
```
## API
In the Android SDK, App-to-App requests are progressed in two main steps : `Proposal`, `Result`

* `Proposal` : A stage that requests a task to be performed by a dApp. There are 5 different types of requests.
* `Result` : A stage to confirm the results of the requested functions

### SDK Initialize
This page describes the procedure to initialize the application of SDK before requesting for App-to-App.

##### WemixWalletSDK()
WemixWalletSDK() initializes SDK

##### Parameters

| Name                  | Type                  | Description                   |
| ---                   | ---                   | ---                           |
| activity or fragment  | Activity or fragment  | Activity or fragment instance |
| proposalResultHandler | ProposalResultHandler | Callback function to receive response. The results can be confirmed by the A2AResponse of the onResult function. |


##### WemixWalletSDK.handleResult()
To receive the result of the request, a user needs to call the corresponding function with the Acitivity or Fragment's onActivityResult function.

##### ProposalResultHandler interface
These are the interfaces that receive the results of a request. Input values when generating WemixWalletSDK.


| Method Name      | Parameters  |Description |
| ---              | ---         | ---        |
| onAuthInitFailed |             |Called when request has failed|
|                  |statusCode   |Error code when request failed (Http Status Code)|
| onNotInstall     |             |Called when WEMIX Wallet is not installed|
|                  |   intent    |Intent sent to the playSt ore|
| onProposalResult |             |Called after user verification|
|                  |  resultCode |After verification:<br> * If verification is successful: Activity.RESULT_OK,<br> * If verification is not successful: Activity.RESULT_CANCELED|
|                  | requestId   |A unique ID for a request|

##### Initializing example
```java
private final ProposalResultHandler resultHandler = new ProposalResultHandler(){
    @Override
    public void onAuthInitFailed(int statusCode){
        // request failed
    }

    @Override
    public void onNotInstall(final Intent intent){
        // WEMIX Wallet not installed. go to store
        startActivity(this, intent);
    }

    @Override
    public void onProposalResult(int resultCode, String requestId) {
        if(resultCode == Activity.RESULT_OK){
            // Request result information on request
            walletSdk.getResult(requestId, new ResponseResultHandler() {
                @Override
                public void onResult(String requestId, A2AResponse response) {
                    // response.getResult().getAddress() <= Only Auth
                    // response.getResult().getTransactionHash()
                }
            });
        }else if(resultCode == Activity.RESULT_CANCELED){
            // user Cancel
        }
    }
}

@Override
protected void onCreate(Bundle savedInstanceState){
    ...

    WemixWalletSDK walletSdk = new WemixWalletSDK(this, resultHandler);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
    if(walletSdk.handleResult(requestCode, resultCode, data)){
        return;
    }
    super.onActivityResult(requestCode, resultCode, data);
}
```

### Proposal
This function uses WemixWalletSDK.proposal(MetaData, SendData) and provides 5 different request types.

* null : request wallet address
* SendWemix : request WEMIX transfer
* SendToken : request Token transfer
* SendNFT : request NFT transfer
* ExecuteContract : request execute contract

##### MetaData

In order to request a Proposal, information about the dApp is required. The information about the dApp must be provided in MetaData class.

##### MetaData(name, description, url, icon)

| Parameter Name | Type | Description | Nullable |
| ---         | ---    | ---                                  |---    |
| name        | String | Name of dApp                         | false |
| description | String | Description of the request(Reserved) | true  |
| url         | String | Main URL of the dApp(Reserved)       | true  |
| icon        | String | URL of the dApp logo(Reserved)       | true  |

```java
Metadata metadata = new Metadata("app name", "description", null, null, null, null);
```

#### Auth
This function requests an authentication of the user’s wallet, and the address of the user wallet can be confirmed when the authentication is completed.

##### Example
```java
walletSdk.proposal(metadata, null);
```

#### SendWemix
This is a request to send the user’s WEMIX to a specific address. After the approval of the request, the user can check the transactionHash of the request.

##### SendWemix(from, to, amount)
| Parameter Name | Type   | Description                                             |
| ---            | ---    | ---                                                     |
| from           | String | Address of the sender (Wallet User Verification Purpose) |
| to             | String | Address of the recipient                                |
| amount         | String | Amount of WEMIX to send **(unit : wei)**                |

##### Example
```java
SendWemix sendWemix = new SendWemix(
    "0x7A8519fE4A25521e4f7692489149BEe8864c6935",
    "0x23a80bdE8dCDDEf6829beD0d5d966BDBf6cB44C3",
    "1000000000000000000" // 1 WEMIX
);
walletSdk.proposal(metadata, sendWemix);
```

#### SendToken
This is a request to send the user’s Token to a specific address. After the approval of the request, the user can check the transactionHash of the request.

##### SendToken(from, to , value, contract)
| Parameter Name | Type   | Description                                             |
| ---            | ---    | ---                                                     |
| from           | String | Address of the sender (Wallet User Verification Purpose) |
| to             | String | Address of the recipient                                |
| value          | String | Amount of WEMIX to send **(including decimal)**         |
| contract       | String | Address of the token                                    |

##### Example
```java
SendToken sendToken = new SendToken(
    "0x7A8519E4A25521e4f7692489149BEe8864c6935",
    "0x23a80bdE8dCDDEf6829beD0d5d966BDBf6cB44C3",
    "10000000000", // In case decimal 10, 1 TOKEN
    "0xF6fF95D53E08c9660dC7820fD5A775484f77183A"
);
walletSdk.proposal(metadata, sendToken);
```

#### SendNFT
This is a request to send the user’s Token to a specific address. After the approval of the request, the user can check the transactionHash of the request.

##### SendNFT(from, to, contract, tokenId)
| Parameter Name | Type   | Description                                             |
| ---            | ---    | ---                                                     |
| from           | String | Address of the sender (Wallet User Verification Purpose) |
| to             | String | Address of the recipient                                |
| contract       | String | Address of the NFT contract                             |
| tokenId        | String | Token ID of the NFT                                     |

##### Example
```java
SendNFT sendNFT = new SendNFT(
    "0x7A8519fE4A25521e4f7692489149BEe8864c6935",
    "0x23a80bdE8dCDDEf6829beD0d5d966BDBf6cB44C3",
    "0xF6fF95D53E08c9660dC7820fD5A775484f77183A",
    "13" // token id
);
walletSdk.proposal(metadata, sendNFT);
```

#### ExecuteContract
This is a request to execute a specific contract. After the approval of the request, the user can check the transactionHash of the request.

##### ContractExecute(from, to, abi, params)
| Parameter Name | Type   | Description                                             |
| ---            | ---    | ---                                                     |
| from           | String | Address of the sender (Wallet User Verification Purpose) |
| to             | String | Address of the contract                                 |
| contract       | String | abi of the function (json string)                       |
| tokenId        | String | Parameters to provide to the function (json string)     |                                   |

##### Example
```java
String abi = "{\"constant\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\" ...";
String params = "[\"0xcad9042cf49684939a2f42c2d916d1b6526635c2\", \"500000000000\"]";
ExecuteContract executeContract = new ExecuteContract(
    "0x7A8519fE4A25521e4f7692489149BEe8864c6935",
    "0xF6fF95D53E08c9660dC7820fD5A775484f77183A",
    abi,
    parmas
);
walletSdk.proposal(metadata, executeContract);
```

## Result
Once the requestId is obtained through ProposalResultHandler.onProposalResult after the user’s approved, the user can confirm the result by the WemixWalletSDK.getResult(requestId, ResponseResultHander) function.

##### ResponseRequestHandler Interface
These are the interfaces returned by the request.

| Method Name | Parameters   | Description                    |
| ---         | ---          | ---                            |
| onResult    |              | Called when request has failed |
|             | requestId    | Unique ID of the request       |
|             | String       | Response to the request        |

```java
walletSdk.getResult(requestId, new ResponseResultHandler() {
    @Override
    public void onResult(String requestId, A2AResponse response) {
        // response.getResult().getAddress() <= Only Auth
        // response.getResult().getTransactionHash()
    }
});
```

## License
WemixWalletSDK-Android is available under the MIT license. See the LICENSE file for more info.
