part of api_client.api;

class EnumerationRepository {
  List<EnumItem> fileCategories = [];

  List<EnumItem> roles = [];

  EnumerationRepository();

  @override
  String toString() {
    return 'EnumerationRepository[fileCategories=$fileCategories, roles=$roles, ]';
  }

  EnumerationRepository.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    fileCategories = EnumItem.listFromJson(json['fileCategories']);
    roles = EnumItem.listFromJson(json['roles']);
  }

  Map<String, dynamic> toJson() {
    return {'fileCategories': fileCategories, 'roles': roles};
  }

  static List<EnumerationRepository> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<EnumerationRepository>()
        : json
            .map((value) => new EnumerationRepository.fromJson(value))
            .toList();
  }

  static Map<String, EnumerationRepository> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, EnumerationRepository>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new EnumerationRepository.fromJson(value));
    }
    return map;
  }
}
