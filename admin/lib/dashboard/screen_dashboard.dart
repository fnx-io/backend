import 'package:admin/app_context.dart';
import 'package:admin/components/gallery_picker/fnx_gallery_picker.dart';
import 'package:angular2/core.dart';

@Component(selector: 'screen-dashboard', templateUrl: 'screen_dashboard.html')
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
