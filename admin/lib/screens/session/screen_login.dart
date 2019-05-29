import 'dart:html';

import 'package:admin/app_context.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:api_client/api.dart';
import 'package:cooky/cooky.dart' as cookie;
import 'package:fnx_rest/fnx_rest_browser.dart';
import 'package:fnx_ui/fnx_ui.dart';

@Component(
  selector: 'screen-login',
  templateUrl: 'screen_login.html',
  directives: [fnxUiDirectives, coreDirectives, formDirectives],
)
class ScreenLogin implements OnInit {
  static const authKeyCookieName = 'fnx_authkey';
  static const authHeader = "Authorization";

  final AppContext ctx;
  final RestClient root;
  final RestClient sessionsRest;
  final FnxApp fnxApp;

  UserLoginDto loginInfo = UserLoginDto();

  var renewPasswordInfo = <String, String>{};

  bool initInProgress = true;

  bool showRenewPasswordForm = false;

  bool showChangePasswordForm = false;

  ScreenLogin(this.ctx, this.root, this.fnxApp)
      : sessionsRest = root.child("/v1/sessions") {}

  @override
  void ngOnInit() {
    initUserWithProgress();
  }

  initUserWithProgress() async {
    initInProgress = true;

    await initUser(ctx, sessionsRest, root);

    initInProgress = false;
  }

  static initUser(
      AppContext ctx, RestClient sessionsRest, RestClient root) async {
    final authKey = cookie.get(authKeyCookieName);
    if (authKey != null) {
      root.setHeader(authHeader, authKey);
      RestResult sessions = await sessionsRest.get();
      if (sessions.success) {
        print("SESSIONS.DATA");
        print(sessions.data);
        ctx.loggedUser = sessions.data;
        ctx.authKey = authKey;
      } else {
        cookie.remove(authKeyCookieName);
      }
    }
  }

  login() async {
    RestResult authKey =
        await sessionsRest.child('/login').post(loginInfo.toJson());
    if (authKey.success) {
      cookie.set(authKeyCookieName, authKey.data);
      initUserWithProgress();
    } else {
      fnxApp.toast('Přihlášení bylo neúspěšné, neplatné jméno nebo heslo.');
    }
  }

  void requestRenewPassword() {
    renewPasswordInfo = {'email': loginInfo.email};
    showChangePasswordForm = false;
    showRenewPasswordForm = true;
  }

  void generateNewPasswordCode(Event event) async {
    event.preventDefault();

    await sessionsRest
        .child('/send-new-password-code')
        .post(renewPasswordInfo['email']);

    showChangePasswordForm = true;
  }

  void renewPassword() async {
    RestResult response =
        await sessionsRest.child('/renew-password').post(renewPasswordInfo);

    if (response.success) {
      fnxApp
          .toast('Změna hesla proběhla úspěšně, přihlaste se s novým heslem.');
      goToLogin();
    } else {
      fnxApp.alert(
          'Změna hesla byla neúspěšná, pravděpodobně zadán neplatný kód.');
    }
  }

  void goToLogin() {
    showRenewPasswordForm = false;
  }

  static void logout(AppContext ctx, RestClient sessionsRest) {
    sessionsRest.delete();
    ctx.loggedUser = null;
    ctx.authKey = null;
    cookie.remove(authKeyCookieName);
  }
}
