# api_client.api.SessionsApi

## Load the API package
```dart
import 'package:api_client/api.dart';
```

All URIs are relative to *http://localhost:8085/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**login**](SessionsApi.md#login) | **POST** /sessions/login | Logs user into the system
[**logout**](SessionsApi.md#logout) | **DELETE** /sessions/logout | Logs out current logged in user session


# **login**
> String login(body)

Logs user into the system



### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new SessionsApi();
var body = new UserLoginDto(); // UserLoginDto | Created user object

try { 
    var result = api_instance.login(body);
    print(result);
} catch (e) {
    print("Exception when calling SessionsApi->login: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserLoginDto**](UserLoginDto.md)| Created user object | 

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **logout**
> logout()

Logs out current logged in user session



### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new SessionsApi();

try { 
    api_instance.logout();
} catch (e) {
    print("Exception when calling SessionsApi->logout: $e\n");
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

