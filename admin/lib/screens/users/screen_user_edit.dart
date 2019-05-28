import 'dart:html';

import 'package:admin/app_context.dart';
import 'package:admin/shared/create_edit_support.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

@Component(
    selector: 'screen-user-edit',
    templateUrl: 'screen_user_edit.html',
    directives: [fnxUiDirectives, coreDirectives, formDirectives])
class ScreenUserEdit with CreateEditSupport implements OnActivate {
  final FnxApp fnxApp;
  final RestClient root;
  RestClient rest;
  final AppContext ctx;
//  final Location location;

  String userId;

  final Router router;

  Map<String, dynamic> user;

  UserEntity typedUser;

  List<EnumItem> roles;

  ScreenUserEdit(this.root, this.router, this.fnxApp, this.ctx) {
    print("USER constructor");
    print(router);
    rest = root.child('/v1/users');
    roles = ctx.enumerations.roles
        .where((r) => r.value != Role.aNONYMOUS_.value)
        .toList();
  }

  void fetchDetails() async {
    RestResult rr = await rest.child('/$userId').get();
    if (rr.success) {
      user = rr.successData;
      typedUser = UserEntity.fromJson(user);
      print('Hura, typed user: $typedUser');
    } else {
      throw rr;
    }
  }

  bool get creating => userId == null || userId == 'create';

  void saveUser(Event e) async {
    e.preventDefault();
    RestResult rr = creating
        ? await rest.post(user)
        : await rest.child("/$userId").put(user);
    if (rr.success) {
      if (creating) {
        fnxApp.toast('User was created.');
      } else {
        fnxApp.toast("User was changed.");
      }
    } else if (rr.data['error'] && rr.data['type'] == 'UniqueValueViolation') {
      fnxApp.alert("This email is already used!");
    } else {
      throw rr;
    }
  }

  @override
  void onActivate(RouterState previous, RouterState current) {
    print("USER onActivate");
    print(current.parameters);
    userId = current.parameters['id'];

    if (isCreate) {
      user = {'roles': 'USER'};
    }
    if (isEdit) {
      fetchDetails();
    }
  }

  @override
  Map get data => user;

  @override
  String get dataId => userId;
}
