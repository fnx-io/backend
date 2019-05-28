# api_client.api.UsersApi

## Load the API package
```dart
import 'package:api_client/api.dart';
```

All URIs are relative to *http://localhost:8085/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create**](UsersApi.md#create) | **POST** /users | Create user
[**delete**](UsersApi.md#delete) | **DELETE** /users/{id} | Delete user
[**getUser**](UsersApi.md#getUser) | **GET** /users/{id} | Get user by id
[**listUsers**](UsersApi.md#listUsers) | **GET** /users | List users
[**register**](UsersApi.md#register) | **POST** /users/register | Register user
[**update**](UsersApi.md#update) | **PUT** /users/{id} | Update user


# **create**
> create(body)

Create user

This can only be done by the logged in user.

### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();
var body = new UserDto(); // UserDto | Created user object

try { 
    api_instance.create(body);
} catch (e) {
    print("Exception when calling UsersApi->create: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserDto**](UserDto.md)| Created user object | 

### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete**
> delete(id)

Delete user

This can only be done by the logged in user.

### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();
var id = 789; // int | The user id that needs to be deleted

try { 
    api_instance.delete(id);
} catch (e) {
    print("Exception when calling UsersApi->delete: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **int**| The user id that needs to be deleted | 

### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUser**
> UserEntity getUser(id)

Get user by id



### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();
var id = 789; // int | The user id.

try { 
    var result = api_instance.getUser(id);
    print(result);
} catch (e) {
    print("Exception when calling UsersApi->getUser: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **int**| The user id. | 

### Return type

[**UserEntity**](UserEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listUsers**
> List<UserEntity> listUsers()

List users

### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();

try { 
    var result = api_instance.listUsers();
    print(result);
} catch (e) {
    print("Exception when calling UsersApi->listUsers: $e\n");
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List<UserEntity>**](UserEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **register**
> UserEntity register(body)

Register user



### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();
var body = new UserDto(); // UserDto | Registered user object

try { 
    var result = api_instance.register(body);
    print(result);
} catch (e) {
    print("Exception when calling UsersApi->register: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserDto**](UserDto.md)| Registered user object | 

### Return type

[**UserEntity**](UserEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update**
> UserEntity update(id, body)

Update user

This can only be done by the logged in user.

### Example 
```dart
import 'package:api_client/api.dart';

var api_instance = new UsersApi();
var id = 789; // int | user id that need to be updated
var body = new UpdateUserDto(); // UpdateUserDto | Updated user object

try { 
    var result = api_instance.update(id, body);
    print(result);
} catch (e) {
    print("Exception when calling UsersApi->update: $e\n");
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **int**| user id that need to be updated | 
 **body** | [**UpdateUserDto**](UpdateUserDto.md)| Updated user object | 

### Return type

[**UserEntity**](UserEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

