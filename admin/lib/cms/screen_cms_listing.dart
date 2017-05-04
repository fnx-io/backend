import 'package:admin/cms/module_cms.dart';
import 'package:admin/util/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

///
/// ScreenNewsList
///
@Component(
  selector: 'screen-cms-listing',
  templateUrl: 'screen_cms_listing.html'
)
class ScreenCmsListing {

  RestClient rest;
  RestListing listing;
  ModuleCms moduleCms;

  String get type => moduleCms.type;

  ScreenCmsListing(RestClient rootRest, this.moduleCms) {
    rest = rootRest.child("/v1/cms/articles?type=${type}");
    listing = RestListingFactory.withPaging(rest);
  }
  
}
