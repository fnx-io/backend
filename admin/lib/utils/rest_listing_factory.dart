import 'package:admin/utils/converted_list_result_driver.dart';
import 'package:fnx_rest/fnx_rest.dart' as rest;

class RestListingFactory {
  static rest.RestListing withPaging(rest.RestClient client) {
    return rest.RestListing(client, rest.ListResultDriver());
  }

  static rest.RestListing withPagingAndConverter(
      rest.RestClient client, ListConverter converter) {
    return rest.RestListing(client, ConvertedListResultDriver(converter));
  }

  static rest.RestListing withoutPaging(rest.RestClient client) {
    return rest.RestListing(client, rest.SimpleListDriver());
  }
}
