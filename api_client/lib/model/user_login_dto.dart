part of api_client.api;

class UserLoginDto {
  String email = null;

  String password = null;

  UserLoginDto();

  @override
  String toString() {
    return 'UserLoginDto[email=$email, password=$password, ]';
  }

  UserLoginDto.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    email = json['email'];
    password = json['password'];
  }

  Map<String, dynamic> toJson() {
    return {'email': email, 'password': password};
  }

  static List<UserLoginDto> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<UserLoginDto>()
        : json.map((value) => new UserLoginDto.fromJson(value)).toList();
  }

  static Map<String, UserLoginDto> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, UserLoginDto>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new UserLoginDto.fromJson(value));
    }
    return map;
  }
}
