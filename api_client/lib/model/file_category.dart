part of api_client.api;

class FileCategory {
  /// The underlying value of this enum member.
  String value;

  FileCategory._internal(this.value);

  static FileCategory iMAGE_ = FileCategory._internal("IMAGE");
  static FileCategory oTHER_ = FileCategory._internal("OTHER");

  FileCategory.fromJson(dynamic data) {
    switch (data) {
      case "IMAGE":
        value = data;
        break;
      case "OTHER":
        value = data;
        break;
      default:
        throw ('Unknown enum value to decode: $data');
    }
  }

  static dynamic encode(FileCategory data) {
    return data.value;
  }
}
