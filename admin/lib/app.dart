import 'dart:html';
import 'package:admin/app_context.dart';

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
  }

  login(user) {
    appContext.loggedUser = user;
  }

  bool isTesting() {
    return window.location.hostname.contains("localhost");
  }

}
