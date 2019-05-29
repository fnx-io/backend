part of api_client.api;

class FileEntity {
  int id = null;

  String name = null;

  String bucketUrl = null;

  String imageUrl = null;

  FileCategory category = null;

/* folder */
  String set_ = null;

  Key uploader = null;

  DateTime uploaded = null;

  String mediaType = null;

  FileEntity();

  @override
  String toString() {
    return 'FileEntity[id=$id, name=$name, bucketUrl=$bucketUrl, imageUrl=$imageUrl, category=$category, set_=$set_, uploader=$uploader, uploaded=$uploaded, mediaType=$mediaType, ]';
  }

  FileEntity.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    id = json['id'];
    name = json['name'];
    bucketUrl = json['bucketUrl'];
    imageUrl = json['imageUrl'];
    category = new FileCategory.fromJson(json['category']);
    set_ = json['set_'];
    uploader = new Key.fromJson(json['uploader']);
    uploaded =
        json['uploaded'] == null ? null : DateTime.parse(json['uploaded']);
    mediaType = json['mediaType'];
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'bucketUrl': bucketUrl,
      'imageUrl': imageUrl,
      'category': category.toJson(),
      'set_': set_,
      'uploader': uploader.toJson(),
      'uploaded': uploaded == null ? '' : uploaded.toUtc().toIso8601String(),
      'mediaType': mediaType
    };
  }

  static List<FileEntity> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<FileEntity>()
        : json.map((value) => new FileEntity.fromJson(value)).toList();
  }

  static Map<String, FileEntity> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, FileEntity>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new FileEntity.fromJson(value));
    }
    return map;
  }
}
