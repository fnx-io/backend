import 'dart:async';
import 'dart:html';
import 'package:admin/cms/module_cms.dart';
import 'package:admin/components/log_listing_modal.dart';
import 'package:admin/components/gallery_picker/fnx_gallery_picker.dart';
import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';
import 'package:admin/model/enums.dart' as enums;

///
/// ScreenNewsEdit
///
@Component(
  selector: 'screen-cms-edit',
  templateUrl: 'screen_cms_edit.html'
)
class ScreenCmsEdit {

  FnxApp fnxApp;
  RestClient rest;
  String id;
  ModuleCms moduleCms;

  Router router;

  Map<String, dynamic> entity;

  String get type => moduleCms.type;

  bool eventLogModalVisible = false;

  RestClient logRest;

  ///
  /// Defines image set. Mainly the ratio you want to enforce in certain types of images.
  ///
  FnxImageSet imageSet = new FnxImageSet("news", "News", 1.3333);

  ScreenCmsEdit(RestClient rootRest, this.router, RouteParams params, this.fnxApp, this.moduleCms) {
    rest = rootRest.child('/v1/cms/articles');
    id = params.get('id');

    if (creating) {
      entity = {'type':'$type', 'data':{}};
    } else {
      fetchDetails();
    }

    logRest = rootRest.child("/v1/cms/articles/${id}/log");
  }

  Future<bool> fetchDetails() async {
    RestResult rr = await rest.child('/$id').get();

    if (rr.success) {
      entity = rr.data;
      if (type != entity['type']) {
        throw "Something is wrong. Received article type '${entity['type']}' != expected '$type'";
      }
      if (entity['data'] == null) entity['data'] = {};
      return true;
    } else {
      return false;
    }
  }

  bool get creating => id == null || id == 'create';

  Future<bool> saveArticle(Event e) async {
    e.preventDefault();
    RestResult rr = await (creating ? rest.post(entity) : rest.child(id).put(entity));
    if (rr.success) {
      fnxApp.toast('Article has been created');
      router.navigate(['CmsListing']);
      return true;
    } else if (rr.data['error']) {
      fnxApp.alert('Error while trying to save article: ${rr.data['error']}');
    } else {
      fnxApp.alert('Error while trying to save article');
    }
    return false;
  }

}
