import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:admin/cms/screen_news_listing.dart';
import 'package:admin/cms/screen_news_edit.dart';


@Component(selector: 'module-news', templateUrl: 'module_news.html')
@RouteConfig(const [
  const Route(path: '/listing', name: 'NewsListing', component: ScreenNewsListing, useAsDefault: true),
  const Route(path: '/edit/:id', name: 'NewsEdit', component: ScreenNewsEdit)
])
class ModuleNews {

  ModuleNews();

}
