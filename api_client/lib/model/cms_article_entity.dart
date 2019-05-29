part of api_client.api;

class CmsArticleEntity {
  int id = null;

  String name = null;

  String type = null;

  DateTime created = null;

  Key createdBy = null;

  String authorName = null;

  Map<String, Object> data = {};

  CmsArticleEntity();

  @override
  String toString() {
    return 'CmsArticleEntity[id=$id, name=$name, type=$type, created=$created, createdBy=$createdBy, authorName=$authorName, data=$data, ]';
  }

  CmsArticleEntity.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    id = json['id'];
    name = json['name'];
    type = json['type'];
    created = json['created'] == null ? null : DateTime.parse(json['created']);
    createdBy = new Key.fromJson(json['createdBy']);
    authorName = json['authorName'];
    data = json['data'];
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'type': type,
      'created': created == null ? '' : created.toUtc().toIso8601String(),
      'createdBy': createdBy.toJson(),
      'authorName': authorName,
      'data': data
    };
  }

  static List<CmsArticleEntity> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<CmsArticleEntity>()
        : json.map((value) => new CmsArticleEntity.fromJson(value)).toList();
  }

  static Map<String, CmsArticleEntity> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, CmsArticleEntity>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new CmsArticleEntity.fromJson(value));
    }
    return map;
  }
}
