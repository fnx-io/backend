import 'dart:html';
import 'package:admin/app_context.dart';
import 'package:admin/auth.dart' as auth;
import 'package:admin/dashboard/screen_dashboard.dart';

import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/errors.dart';
import 'package:logging/logging.dart';
///
/// Root komponenta aplikace. Stará se o zobrazování errorů, ale především definuje routovací
/// pravidla pro další moduly.
///
///
@Component(selector: 'app', templateUrl: 'app.html')
@RouteConfig(const [
  const Route(path: '/dashboard', name: 'Dashboard', component: ScreenDashboard, useAsDefault: true)
])
class App {

  final Logger log = new Logger("App");

  String errorHeadline = null;
  String errorMessage = null;
  List<String> errorList = [];

  RestClient rootRestClient = null;

  AppContext appContext;

  Router router;

  App(this.rootRestClient, FnxExceptionHandler exceptionHandler, this.appContext, Router router) {
    exceptionHandler.setShowErrorCallback(showError);
    this.router = router;
  }

  void authTokenChanged(String token) {
    auth.saveLoginToken(token);
    rootRestClient.setHeader('Authorization', token);
  }

  void loggedUserChanged(Map<String, dynamic> user) {
    appContext.loggedUser = user;
  }

  void showError(FnxError err) {
    log.info("Showing error: ${err.headline}");
    errorHeadline = err.headline;
    errorMessage = err.message;
    errorList = err.details;
  }

  closeError() {
    errorHeadline = null;
    errorMessage = null;
  }

  logout() {
    appContext.logout();
    auth.removeLoginToken();
  }

  bool isTesting() {
    return window.location.hostname.contains("localhost");
  }

}
