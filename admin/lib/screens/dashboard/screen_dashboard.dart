import 'package:admin/app_context.dart';
import 'package:admin/components/gallery_picker/fnx_gallery_picker.dart';
import 'package:angular/angular.dart';
import 'package:fnx_ui/fnx_ui.dart';

@Component(selector: 'screen-dashboard', templateUrl: 'screen_dashboard.html', directives: [fnxUiAllDirectives, coreDirectives, FnxGalleryPicker])
class ScreenDashboard {
  AppContext appCtx;

  ScreenDashboard(this.appCtx);

  String imgUrl;

  FnxImageSet imageSet = new FnxImageSet("news", "News", 1.3333);

  void galleryImagePicked(String url) {
    print("Caught: $url");
    imgUrl = url;
  }
}
