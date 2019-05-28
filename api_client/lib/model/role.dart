part of api_client.api;

class Role {
  /// The underlying value of this enum member.
  String value;

  Role._internal(this.value);

  /// Role types
  static Role aDMIN_ = Role._internal("ADMIN");

  /// Role types
  static Role uSER_ = Role._internal("USER");

  /// Role types
  static Role aNONYMOUS_ = Role._internal("ANONYMOUS");

  Role.fromJson(dynamic data) {
    switch (data) {
      case "ADMIN":
        value = data;
        break;
      case "USER":
        value = data;
        break;
      case "ANONYMOUS":
        value = data;
        break;
      default:
        throw ('Unknown enum value to decode: $data');
    }
  }

  static dynamic encode(Role data) {
    return data.value;
  }

  static List<Role> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<Role>()
        : json.map((value) => new Role.fromJson(value)).toList();
  }
}
