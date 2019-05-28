import 'package:api_client/api.dart';

///
/// Repozitář pro jeden číselník, kromě property 'all' má ještě
/// operátor [] takže se dá na hodnotu číselníku sáhnout přes ID jako do mapy.
///
class EnumerationRepositoryExt {
  List<EnumItem> all;
  Map<dynamic, EnumItem> _byId;

  EnumerationRepositoryExt.fromJson(List<dynamic> data) {
    all = [];
    _byId = {};
    data.forEach((dynamic row) {
      EnumItem val = new EnumItem.fromJson(row);
      all.add(val);
      _byId[val.value] = val;
    });
  }

  EnumItem operator [](dynamic key) {
    return _byId[key];
  }

  String toString() {
    return _byId.toString();
  }

  static Map<String, EnumerationRepositoryExt> buildFromAllEnumerations(
      Map data) {
    Map<String, EnumerationRepositoryExt> result = {};
    data.keys.forEach((dynamic enumeration) {
      result[enumeration] =
          new EnumerationRepositoryExt.fromJson(data[enumeration]);
    });
    return result;
  }
}
