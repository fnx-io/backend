import 'package:angular_router/angular_router.dart';
import 'package:admin/screens/dashboard/screen_dashboard.template.dart';
import 'package:admin/screens/users/screen_user_list.template.dart';
import 'package:admin/screens/users/screen_user_edit.template.dart';
import 'package:admin/screens/cms/screen_cms_list.template.dart';
import 'package:admin/screens/cms/screen_cms_edit.template.dart';

class Routing {
  //cesty
  RoutePath dashboard = new RoutePath(path: "dashboard", parent: null);

  RoutePath usersList = new RoutePath(path: "users-list", parent: null);
  RoutePath userEdit = new RoutePath(path: "user-edit/:id", parent: null);

  RoutePath cmsList = new RoutePath(path: "cms/:type", parent: null);
  RoutePath cmsEdit = new RoutePath(path: "cms/:type/edit/:id", parent: null);

  //Mapovani cesty na komponenty
  List<RouteDefinition> routes = [];

  Routing() {
    routes.add(RouteDefinition.redirect(path: '', redirectTo: dashboard.toUrl()));
    routes.add(new RouteDefinition(
        routePath: dashboard,
        component: ScreenDashboardNgFactory,
        useAsDefault: true));
    routes.add(new RouteDefinition(
        routePath: usersList,
        component: ScreenUserListNgFactory));
    routes.add(new RouteDefinition(
        routePath: userEdit,
        component: ScreenUserEditNgFactory));
    routes.add(new RouteDefinition(
        routePath: cmsList,
        component: ScreenCmsListNgFactory));
    routes.add(new RouteDefinition(
        routePath: cmsEdit,
        component: ScreenCmsEditNgFactory));
  }

}
