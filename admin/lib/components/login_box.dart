import 'dart:async';

import 'package:admin/util/auth.dart' as auth;
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'login-box',
  templateUrl: 'login_box.html'
)
class LoginBox implements OnInit {
  Map<String, dynamic> formData = {};

  RestClient rootRest;
  RestClient rest;

  String message;

  @Output() EventEmitter<String> token = new EventEmitter<String>();
  @Output() EventEmitter<Map<String, dynamic>> user = new EventEmitter<Map<String, dynamic>>();

  LoginBox(RestClient rootRest) {
    this.rest = rootRest.child("/v1/users/session");
  }

  Future<bool> onSubmit() async {
    message = null;
    RestResult rr = await this.rest.child("?admin=true").post(formData);
    if (rr.success) {
      String authToken = rr.data['token'];
      Map<String, dynamic> loggedUser = rr.data['user'];
      token.emit(authToken);
      user.emit(loggedUser);
      return true;
    } else if (rr.status == 401) {
      message = "No such user exists, the password was invalid, or the user is not an admin";
    }

    return false;
  }

  @override
  ngOnInit() {
    tryLoginFromStoredToken();
  }

  Future<bool> tryLoginFromStoredToken() async {
    final String authToken = await auth.loadLoginToken();
    if (authToken == null || authToken.isEmpty) return false;

    final RestResult rr = await rest.child("").get(headers: {'Authorization': authToken});
    if (rr.success) {
      token.emit(authToken);
      user.emit(rr.data);
      return true;
    } else {
      auth.removeLoginToken();
      return false;
    }
  }
}
