part of api_client.api;

class Key {
    Key();

  @override
  String toString() {
    return 'Key[]';
  }

  Key.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
  }

  Map<String, dynamic> toJson() {
    return {
     };
  }

  static List<Key> listFromJson(List<dynamic> json) {
    return json == null ? new List<Key>() : json.map((value) => new Key.fromJson(value)).toList();
  }

  static Map<String, Key> mapFromJson(Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, Key>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) => map[key] = new Key.fromJson(value));
    }
    return map;
  }
}

