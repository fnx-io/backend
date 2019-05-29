part of api_client.api;

class UserDto {
  String email = null;

  String firstName = null;

  String lastName = null;

  String password = null;

  List<Role> roles = [];

  UserDto();

  @override
  String toString() {
    return 'UserDto[email=$email, firstName=$firstName, lastName=$lastName, password=$password, roles=$roles, ]';
  }

  UserDto.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    email = json['email'];
    firstName = json['firstName'];
    lastName = json['lastName'];
    password = json['password'];
    roles = Role.listFromJson(json['roles']);
  }

  Map<String, dynamic> toJson() {
    return {
      'email': email,
      'firstName': firstName,
      'lastName': lastName,
      'password': password,
      'roles': roles.map((i) => i.toJson()).toList()
    };
  }

  static List<UserDto> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<UserDto>()
        : json.map((value) => new UserDto.fromJson(value)).toList();
  }

  static Map<String, UserDto> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, UserDto>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new UserDto.fromJson(value));
    }
    return map;
  }
}
