import 'package:admin/app_context.dart';
import 'package:angular2/core.dart';

@Component(selector: 'screen-dashboard', templateUrl: 'screen_dashboard.html')
class ScreenDashboard {

  AppContext appCtx;

  ScreenDashboard(this.appCtx);
}
