import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'fnx-gallery-picker',
  templateUrl: 'fnx_gallery_picker.html'
)
class FnxGalleryPicker {

  RestClient rest;
  RestListing listing;

  FnxGalleryPicker(RestClient rest) {
    this.rest = rest.child("/v1/files?category=IMAGE");
    this.listing = RestListingFactory.withPaging(this.rest);
  }

  String thumbnailUrl(Map<String, dynamic> image) {
    if (image == null) return null;
    if (image['imageUrl'] != null) return "${image['imageUrl']}=s170";

    return image['bucketUrl'];
  }
}
