import 'dart:async';
import 'dart:html';
import 'package:angular2/core.dart';
import 'package:angular2/router.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';
import 'package:admin/model/enums.dart' as enums;

///
/// ScreenNewsEdit
///
@Component(
  selector: 'screen-news-edit',
  templateUrl: 'screen_news_edit.html'
)
class ScreenNewsEdit {

  FnxApp fnxApp;
  RestClient rest;
  String id;

  Router router;

  Map<String, dynamic> entity;

  ScreenNewsEdit(RestClient rootRest, this.router, RouteParams params, this.fnxApp) {
    rest = rootRest.child('/v1/cms/articles');
    id = params.get('id');

    if (creating) {
      entity = {'type':'news', 'data':{}};
    } else {
      fetchDetails();
    }
  }

  Future<bool> fetchDetails() async {
    RestResult rr = await rest.child('/$id').get();

    if (rr.success) {
      entity = rr.data;
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
      router.navigate(['NewsListing']);
      return true;
    } else if (rr.data['error'] && rr.data['type'] == 'UniqueValueViolation') {
      fnxApp.alert('User with such email already exists!');
    } else {
      fnxApp.alert('Error while trying to save user');
    }
    return false;
  }

}
