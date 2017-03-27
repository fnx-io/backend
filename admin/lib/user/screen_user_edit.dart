import 'package:fnx_ui/fnx_ui.dart';
import 'dart:async';
import 'dart:html';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:admin/model/enums.dart' as enums;

@Component(
    selector: 'screen-user-edit',
    templateUrl: 'screen_user_edit.html'
)
class ScreenUserEdit {

  FnxApp fnxApp;
  RestClient rest;
  String id;

  Router router;

  Map<String, dynamic> entity;

  List<enums.UserRole> roles = enums.UserRole.ALL_ROLES;

  ScreenUserEdit(RestClient rootRest, this.router, RouteParams params, this.fnxApp) {
    rest = rootRest.child('/v1/users');
    id = params.get('id');

    if (creating) {
      entity = {'role': 'USER'};
    } else {
      fetchDetails();
    }
  }

  Future<bool> fetchDetails() async {
    RestResult rr = await rest.child('/$id').get();

    if (rr.success) {
      entity = rr.data;
      return true;
    } else {
      throw rr;
    }
  }

  bool get creating => id == null || id == 'create';

  Future<bool> saveUser(Event e) async {
    e.preventDefault();
    RestResult rr = creating ? await rest.post(entity) : await rest.child("/$id").put(entity);
    if (rr.success) {
      if (creating) {
        fnxApp.toast('User was created.');
      } else {
        fnxApp.toast("User was changed.");
      }
      router.navigate(['UserListing']);
      return true;
    } else if (rr.data['error'] && rr.data['type'] == 'UniqueValueViolation') {
      fnxApp.alert("This email is already used!");
    } else {
      throw rr;
    }
  }


}
