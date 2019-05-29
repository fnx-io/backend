import 'package:fnx_rest/fnx_rest.dart';

typedef ListConverter = List Function(List);

/// Expects the API to return a Map with 'data' attribute.
/// Converts received data with given function
class ConvertedListResultDriver implements RestListingDriver {
  final ListConverter converter;

  ConvertedListResultDriver(this.converter);

  @override
  RestClient prepareClient(RestClient client, int page) {
    return SimpleListDriver.queryParamPager(client, page);
  }

  @override
  UnpackedData unpackData(dynamic data) {
    if (data is Map) {
      if (data != null && data['data'] is! List) {
        throw "Expected 'data' key to be a List, but was $data['data']";
      }
      List<dynamic> list = data['data'] ?? [];
      return new UnpackedData(list.isNotEmpty, converter(list));
    } else {
      throw "Expected API result to be a map with 'data' key containing results";
    }
  }
}
