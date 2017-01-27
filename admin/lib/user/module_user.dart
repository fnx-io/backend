import 'package:admin/user/screen_user_listing.dart';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';

@Component(selector: 'module-user', templateUrl: 'module_user.html')
@RouteConfig(const [
  const Route(path: '/listing', name: 'UserListing', component: ScreenUserListing, useAsDefault: true)
])
class ModuleUser {

}
