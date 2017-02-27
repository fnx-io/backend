import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:admin/cms/screen_cms_listing.dart';
import 'package:admin/cms/screen_cms_edit.dart';


@Component(selector: 'module-cms', templateUrl: 'module_cms.html')
@RouteConfig(const [
  const Route(path: '/listing', name: 'CmsListing', component: ScreenCmsListing, useAsDefault: true),
  const Route(path: '/edit/:id', name: 'CmsEdit', component: ScreenCmsEdit)
])
class ModuleCms implements CanReuse {

  RouteParams params;

  ///
  /// Article "type" defines a set of articles with the same structure.
  /// Use this value to customize article structure on "screen edit"
  ///
  String type;

  ModuleCms(this.params) {
    type = params.get("type")?.toString();
    if (type == null) {
      throw "Please provide article type as a routing parameter";
    }
  }

  @override
  bool routerCanReuse(ComponentInstruction nextInstruction, ComponentInstruction prevInstruction) {
    return false;
  }
}
