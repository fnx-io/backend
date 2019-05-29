part of api_client.api;

class UpdateUserDto {
  String email = null;

  String firstName = null;

  String lastName = null;

  int userId = null;

  String password = null;

  List<Role> roles = [];

  UpdateUserDto();

  @override
  String toString() {
    return 'UpdateUserDto[email=$email, firstName=$firstName, lastName=$lastName, userId=$userId, password=$password, roles=$roles, ]';
  }

  UpdateUserDto.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    email = json['email'];
    firstName = json['firstName'];
    lastName = json['lastName'];
    userId = json['userId'];
    password = json['password'];
    roles = Role.listFromJson(json['roles']);
  }

  Map<String, dynamic> toJson() {
    return {
      'email': email,
      'firstName': firstName,
      'lastName': lastName,
      'userId': userId,
      'password': password,
      'roles': roles?.map((i) => i.toJson())?.toList()
    };
  }

  static List<UpdateUserDto> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<UpdateUserDto>()
        : json.map((value) => new UpdateUserDto.fromJson(value)).toList();
  }

  static Map<String, UpdateUserDto> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, UpdateUserDto>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new UpdateUserDto.fromJson(value));
    }
    return map;
  }
}
