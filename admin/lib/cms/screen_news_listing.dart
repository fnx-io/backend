import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';

///
/// ScreenNewsList
///
@Component(
  selector: 'screen-news-listing',
  templateUrl: 'screen_news_listing.html'
)
class ScreenNewsListing {

  RestClient rest;
  RestListing listing;

  ScreenNewsListing(RestClient rootRest) {
    rest = rootRest.child("/v1/cms/articles?type=news");
    listing = RestListingFactory.withPaging(rest);
  }
  
}
