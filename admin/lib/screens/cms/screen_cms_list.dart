import 'package:admin/routing.dart';
import 'package:admin/utils/rest_listing_factory.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

///
/// ScreenNewsList
///
@Component(
    selector: 'screen-cms-listing',
    templateUrl: 'screen_cms_list.html',
    directives: [fnxUiDirectives, coreDirectives])
class ScreenCmsList implements OnActivate {
  final RestClient root;
  RestClient rest;
  RestListing listing;
  final Router router;
  final Routing routing;

  String type;

  ScreenCmsList(this.root, this.router, this.routing) {
    print("constructor");
  }

  @override
  void onActivate(RouterState previous, RouterState current) {
    print("trying to get type");
    type = current.parameters['type'];
    print("type");
    print(type);

    rest = root.child("/v1/cms/articles?type=${type}");
    listing = RestListingFactory.withPagingAndConverter(
        rest, (jsonArticles) => CmsArticleEntity.listFromJson(jsonArticles));
  }

  createRecord() {
    router.navigate(
        routing.cmsEdit.toUrl(parameters: {'type': type, 'id': 'create'}));
  }

  editRecord(CmsArticleEntity a) {
    router.navigate(routing.cmsEdit
        .toUrl(parameters: {'type': type, 'id': a.id.toString()}));
  }

  List<CmsArticleEntity> get articles => listing.list.cast<CmsArticleEntity>();
}
