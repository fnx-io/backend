part of api_client.api;

class FileCategory {
  /// The underlying value of this enum member.
  String value;

  FileCategory._internal(this.value);

  static FileCategory iMAGE_ = FileCategory._internal("IMAGE");
  static FileCategory oTHER_ = FileCategory._internal("OTHER");

  FileCategory.fromJson(dynamic data) {
    switch (data) {
          case "IMAGE": value = data; break;
          case "OTHER": value = data; break;
    default: throw('Unknown enum value to decode: $data');
    }
  }

  static dynamic encode(FileCategory data) {
    return data.value;
  }

  String toJson() {
    return FileCategory.encode(this);
  }

  static List<FileCategory> listFromJson(List<dynamic> json) {
    return json == null ? new List<FileCategory>() : json.map((value) => new FileCategory.fromJson(value)).toList();
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is FileCategory &&
              runtimeType == other.runtimeType &&
              value == other.value;

  @override
  int get hashCode => value.hashCode;

}

