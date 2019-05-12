import 'dart:html';

import 'package:admin/app_context.dart';
import 'package:admin/model/enum_item.dart';
import 'package:admin/shared/create_edit_support.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

@Component(
    selector: 'screen-user-edit',
    templateUrl: 'screen_user_edit.html',
    directives: [fnxUiDirectives,
    coreDirectives, formDirectives]
)
class ScreenUserEdit with CreateEditSupport implements OnActivate {

  final FnxApp fnxApp;
  final RestClient root;
  RestClient rest;
  final AppContext ctx;
  final Location location;

  String userId;

  final Router router;


  Map<String, dynamic> user;

  List<EnumItem> roles;

  ScreenUserEdit(this.root, this.router, this.fnxApp, this.ctx, this.location) {
    print("USER constructor");
    print(router);
    rest = root.child('/v1/users');
    roles = ctx.enumerations['roles'].all.where((EnumItem i) => i.value != 'ANONYMOUS').toList();
  }



  void fetchDetails() async {
    RestResult rr = await rest.child('/$userId').get();
    if (rr.success) {
      user = rr.successData;
    } else {
      throw rr;
    }
  }

  bool get creating => userId == null || userId == 'create';

  void saveUser(Event e) async {
    e.preventDefault();
    RestResult rr = creating ? await rest.post(user) : await rest.child("/$userId").put(user);
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
      user = {'role': 'USER'};
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
