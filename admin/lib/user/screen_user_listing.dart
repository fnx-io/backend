import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'screen-user-listing',
  templateUrl: 'screen_user_listing.html'
)
class ScreenUserListing {

  RestClient rest;
  RestListing listing;

  ScreenUserListing(RestClient rootRest) {
    rest = rootRest.child("/v1/users");
    listing = RestListingFactory.withPaging(rest);
  }
}
