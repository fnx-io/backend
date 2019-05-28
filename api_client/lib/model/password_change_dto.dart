part of api_client.api;

class PasswordChangeDto {
  String email = null;

  String password = null;

  String token = null;

  PasswordChangeDto();

  @override
  String toString() {
    return 'PasswordChangeDto[email=$email, password=$password, token=$token, ]';
  }

  PasswordChangeDto.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    email = json['email'];
    password = json['password'];
    token = json['token'];
  }

  Map<String, dynamic> toJson() {
    return {'email': email, 'password': password, 'token': token};
  }

  static List<PasswordChangeDto> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<PasswordChangeDto>()
        : json.map((value) => new PasswordChangeDto.fromJson(value)).toList();
  }

  static Map<String, PasswordChangeDto> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, PasswordChangeDto>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new PasswordChangeDto.fromJson(value));
    }
    return map;
  }
}
