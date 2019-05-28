import 'package:admin/app_context.dart';
import 'package:admin/routing.dart';
import 'package:admin/utils/rest_listing_factory.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

@Component(
    selector: 'screen-user-list',
    templateUrl: 'screen_user_list.html',
    directives: [fnxUiDirectives, coreDirectives])
class ScreenUserList {
  final AppContext ctx;
  final RestClient root;
  RestClient rest;
  RestListing listing;

  final Router router;
  final Routing routing;

  ScreenUserList(this.ctx, this.root, this.router, this.routing) {
    rest = root.child("/v1/users");
    listing = RestListingFactory.withPaging(rest);
  }

  goToDetail(UserEntity user) {
    router.navigate(
        routing.userEdit.toUrl(parameters: {"id": user.id.toString()}));
  }

  createUser() {
    router.navigate(routing.userEdit.toUrl(parameters: {"id": "create"}));
  }

  List<EnumItem> get roles => ctx.enumerations.roles;

  List<UserEntity> get users => UserEntity.listFromJson(listing.list);

  String rolesLabel(UserEntity user) =>
      user.roles.map((r) => r.value).join(',');
}
