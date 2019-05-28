part of api_client.api;

class ClientConfiguration {
  Map<String, Object> messages = {};

  EnumerationRepository enumerations = null;

  ClientConfiguration();

  @override
  String toString() {
    return 'ClientConfiguration[messages=$messages, enumerations=$enumerations, ]';
  }

  ClientConfiguration.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    messages = json['messages'];
    enumerations = new EnumerationRepository.fromJson(json['enumerations']);
  }

  Map<String, dynamic> toJson() {
    return {'messages': messages, 'enumerations': enumerations};
  }

  static List<ClientConfiguration> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<ClientConfiguration>()
        : json.map((value) => new ClientConfiguration.fromJson(value)).toList();
  }

  static Map<String, ClientConfiguration> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, ClientConfiguration>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new ClientConfiguration.fromJson(value));
    }
    return map;
  }
}
