import 'package:admin/app_context.dart';
import 'package:admin/messages/test.dart';
import 'package:admin/routing.dart';
import 'package:admin/screens/session/screen_login.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/components/fnx_app/fnx_app.dart';
import 'package:fnx_ui/fnx_ui.dart';
import 'package:logging/logging.dart';

@Component(selector: 'app', templateUrl: 'app.html', directives: [fnxUiAllDirectives, coreDirectives, formDirectives, routerDirectives, ScreenLogin])
class App {
  final Logger log = new Logger("App");

  @ViewChild(FnxApp)
  FnxApp app;

  final AppContext ctx;
  final Routing routing;
  final Router router;
  final RestClient root;

  static Map cmsRouteParams = {
    "news": {"type": "news"},
    "events": {"type": "events"},
  };

  App(this.ctx, this.routing, this.router, this.root) {
    print("Message test: " + ctx.msg.app.backend);
  }

  logout() {
    ScreenLogin.logout(ctx, root.child("/v1/sessions"));
  }

  bool isTesting() {
    return ctx.local;
  }
}
