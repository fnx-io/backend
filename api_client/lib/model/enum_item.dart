part of api_client.api;

class EnumItem {
  
  String value = null;
  

  String label = null;
  
  EnumItem();

  @override
  String toString() {
    return 'EnumItem[value=$value, label=$label, ]';
  }

  EnumItem.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    value =
        json['value']
    ;
    label =
        json['label']
    ;
  }

  Map<String, dynamic> toJson() {
    return {
      'value': value,
      'label': label
     };
  }

  static List<EnumItem> listFromJson(List<dynamic> json) {
    return json == null ? new List<EnumItem>() : json.map((value) => new EnumItem.fromJson(value)).toList();
  }

  static Map<String, EnumItem> mapFromJson(Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, EnumItem>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) => map[key] = new EnumItem.fromJson(value));
    }
    return map;
  }
}

