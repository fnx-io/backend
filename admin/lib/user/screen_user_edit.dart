import 'dart:async';
import 'dart:html';

import 'package:admin/model/enum_item.dart';
import 'package:admin/util/app_context.dart';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

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

  List<EnumItem> roles;

  ScreenUserEdit(RestClient rootRest, this.router, RouteParams params, this.fnxApp, AppContext ctx) {
    rest = rootRest.child('/v1/users');
    id = params.get('id');
    roles = ctx.enumerations['roles'].all.where((EnumItem i) => i.value != 'ANONYMOUS').toList();

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
