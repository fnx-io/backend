import 'dart:async';
import 'package:cookie/cookie.dart' as cookie;

const COOKIE_NAME = "auth_key";

Future<String> loadLoginToken() {
  return new Future.value(cookie.get(COOKIE_NAME));
}

Future<bool> saveLoginToken(String token) {
  cookie.set(COOKIE_NAME, token, expires: new DateTime.now().add(new Duration(days: 14)), path: "/");
  return new Future.value(true);
}

Future<bool> removeLoginToken() {
  cookie.remove(COOKIE_NAME);
  return new Future.value(true);
}
