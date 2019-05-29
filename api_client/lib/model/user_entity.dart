part of api_client.api;

class UserEntity {
  
  int id = null;
  

  String email = null;
  

  String firstName = null;
  

  String lastName = null;
  

  String password = null;
  

  List<Role> roles = [];
  

  String avatarUrl = null;
  
  UserEntity();

  @override
  String toString() {
    return 'UserEntity[id=$id, email=$email, firstName=$firstName, lastName=$lastName, password=$password, roles=$roles, avatarUrl=$avatarUrl, ]';
  }

  UserEntity.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    id =
        json['id']
    ;
    email =
        json['email']
    ;
    firstName =
        json['firstName']
    ;
    lastName =
        json['lastName']
    ;
    password =
        json['password']
    ;
    roles =
      Role.listFromJson(json['roles'])
;
    avatarUrl =
        json['avatarUrl']
    ;
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'firstName': firstName,
      'lastName': lastName,
      'password': password,
      'roles': roles,
      'avatarUrl': avatarUrl
     };
  }

  static List<UserEntity> listFromJson(List<dynamic> json) {
    return json == null ? new List<UserEntity>() : json.map((value) => new UserEntity.fromJson(value)).toList();
  }

  static Map<String, UserEntity> mapFromJson(Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, UserEntity>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) => map[key] = new UserEntity.fromJson(value));
    }
    return map;
  }
}

