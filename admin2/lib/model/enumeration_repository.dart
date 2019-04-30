import 'package:admin/model/enum_item.dart';


///
/// Repozitář pro jeden číselník, kromě property 'all' má ještě
/// operátor [] takže se dá na hodnotu číselníku sáhnout přes ID jako do mapy.
///
class EnumerationRepository {

  List<EnumItem> all;
  Map<dynamic, EnumItem> _byId;

  EnumerationRepository.fromJson(List<dynamic> data) {
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

  static Map<String,EnumerationRepository> buildFromAllEnumerations(Map data) {
    Map<String,EnumerationRepository> result = {};
    data.keys.forEach((dynamic enumeration) {
      result[enumeration] = new EnumerationRepository.fromJson(data[enumeration]);
    });
    return result;
  }

}