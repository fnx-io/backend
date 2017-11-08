import 'package:admin/model/enumeration_repository.dart';
import 'package:admin/util/app_context.dart';
import 'package:admin/util/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'screen-user-listing',
  templateUrl: 'screen_user_listing.html'
)
class ScreenUserListing {

  RestClient rest;
  RestListing listing;
  EnumerationRepository roles;

  ScreenUserListing(RestClient rootRest, AppContext ctx) {
    rest = rootRest.child("/v1/users");
    listing = RestListingFactory.withPaging(rest);
    roles = ctx.enumerations['roles'];
  }
}
