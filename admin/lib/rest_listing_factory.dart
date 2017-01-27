import 'package:fnx_rest/fnx_rest.dart' as rest;

class RestListingFactory {
  static rest.RestListing withPaging(rest.RestClient client) {
    return new rest.RestListing(client, new rest.ListResultDriver());
  }

  static rest.RestListing withoutPaging(rest.RestClient client) {
    return new rest.RestListing(client, new rest.SimpleListDriver());
  }
}
