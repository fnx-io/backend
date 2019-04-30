

abstract class CreateEditSupport {

  String get dataId;

  Map get data;

  bool get isNotEmpty => data != null;

  bool get isCreate =>
      dataId != null && dataId.toLowerCase().contains('create');

  bool get isEdit => dataId != null && !isCreate;

}