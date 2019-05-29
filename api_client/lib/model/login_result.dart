part of api_client.api;

class LoginResult {
  bool success = null;

  String token = null;

  UserEntity user = null;

  LoginResult();

  @override
  String toString() {
    return 'LoginResult[success=$success, token=$token, user=$user, ]';
  }

  LoginResult.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    success = json['success'];
    token = json['token'];
    user = new UserEntity.fromJson(json['user']);
  }

  Map<String, dynamic> toJson() {
    return {'success': success, 'token': token, 'user': user.toJson()};
  }

  static List<LoginResult> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<LoginResult>()
        : json.map((value) => new LoginResult.fromJson(value)).toList();
  }

  static Map<String, LoginResult> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, LoginResult>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new LoginResult.fromJson(value));
    }
    return map;
  }
}
