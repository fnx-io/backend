import 'dart:async';
import 'dart:html';
import 'dart:typed_data';

import 'package:admin/utils/rest_listing_factory.dart';
import 'package:angular/angular.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

///
/// Defines set of images. Used to keep similar images together.
///
class FnxImageSet {

  final String set;
  final String name;
  final double ratio;

  FnxImageSet(this.set, this.name, this.ratio);

}


@Component(
  selector: 'fnx-gallery-picker',
  templateUrl: 'fnx_gallery_picker.html',
  directives: const [ PickImageStageComponent,
                      CropImageComponent, fnxUiDirectives,
  coreDirectives]
)
class FnxGalleryPicker implements OnInit {

  RestClient rest;
  RestListing listing;

  ImageUploadStage stage = ImageUploadStage.PICK;

  ImageRead imageRead;

  String errorMessage;

  FnxApp fnxApp;

  @Input() FnxImageSet imageSet;

  @ViewChild('pickImageTab') FnxTab pickImageTab;

  final _picked = new StreamController<String>();
  @Output() Stream<String> get picked => _picked.stream;

  bool get requiresCrop => imageSet != null && imageSet.ratio != null;

  FnxGalleryPicker(RestClient rest, this.fnxApp) {
    this.rest = rest.child("/v1/files");
  }


  @override
  void ngOnInit() {
    if (imageSet == null) throw "You must specify 'set' attribute (instance of FnxImageSet)";
    this.listing = RestListingFactory.withPaging(this.rest.child("?category=IMAGE&set=${imageSet.set}"));
  }

  String thumbnailUrl(Map<String, dynamic> image) {
    if (image == null) return null;
    if (image['imageUrl'] != null) return "${image['imageUrl']}=s170";

    return image['bucketUrl'];
  }

  void imagePicked(ImageRead img) {
    errorMessage = null;
    imageRead = img;
    if (requiresCrop) {
      stage = ImageUploadStage.CROP;
    } else {
      imageCropped(null);
    }
  }

  void imageCropped(Rectangle<double> crop) {
    errorMessage = null;
    stage = ImageUploadStage.UPLOAD;
    final FileReader fr = new FileReader();
    fr.readAsArrayBuffer(imageRead.file);
    fr.onLoad.listen((_) => uploadImage(imageRead.file, fr.result, crop));
    fr.onError.listen((_) => this.errorMessage = "File ${imageRead.file.name} could not be loaded.");
  }

  Future<bool> uploadImage(File file, Uint8List fileContents, Rectangle<double> crop) async {
    RestClient rc = this.rest.child("/image").producesBinary(file.type);
    rc = rc.setParam("set", imageSet.set);

    if (crop != null) {
      rc = rc.setParam("x", crop.left.toString());
      rc = rc.setParam("y", crop.top.toString());
      rc = rc.setParam("widthRatio", crop.width.toString());
      rc = rc.setParam("heightRatio", crop.height.toString());
    }

    RestResult rr;
    try {
      rr = await rc.post(fileContents, headers: {'X-Filename': file.name, 'Content-Type': file.type});
    } catch (e) {
      rr = null;
    }

    stage = ImageUploadStage.PICK;
    if (rr != null && rr.success) {
      fnxApp.toast("Image has been uploaded.");
      _picked.add(rr.data['imageUrl']);
      /*
      if (pickImageTab != null) pickImageTab.selectTab();
      print(rr.data['imageUrl']);
      listing.refresh();
      */
      return true;
    } else {
      errorMessage = "We encountered an error during the upload of your image";
      stage = ImageUploadStage.CROP;
      return false;
    }
  }

  void galleryImagePicked(Map<String, dynamic> imgRecord) {
    if (imgRecord == null) return;
    String url = imgRecord['imageUrl'];
    if (url == null) url = imgRecord['bucketUrl'];
    if (url == null) return;

    print("Picked! $url");

    _picked.add(url);
  }

  bool get showPickImageStage => stage == null || stage == ImageUploadStage.PICK;
  bool get showCropImageStage => stage == ImageUploadStage.CROP;
  bool get showImageUploadStage => stage == ImageUploadStage.UPLOAD;
}

enum ImageUploadStage {
  PICK,
  CROP,
  UPLOAD
}

@Component(
  selector: 'pick-image-component',
  templateUrl: 'pick_image_component.html',
  directives: [fnxUiDirectives,
  coreDirectives]
)
class PickImageStageComponent {

  String errorMessage;

  final _image = new StreamController<ImageRead>();
  @Output() Stream<ImageRead> get image => _image.stream;

  void filePicked(File file) {
    errorMessage = null;
    if (file == null) {
      errorMessage = 'Please pick an image to upload.';
      return;
    } else if (!validFile(file)) {
      errorMessage = "File ${file.name} is not supported image.";
      return;
    }
    final FileReader fr = new FileReader();
    fr.readAsDataUrl(file);
    fr.onLoad.listen((_) => this._image.add(new ImageRead(file, fr.result)));
    fr.onError.listen((_) => this.errorMessage = "File ${file.name} could not be loaded.");
  }

  bool validFile(File f) {
    if (f == null) return false;
    if (f.type == null) return false;

    return f.type.startsWith("image");
  }
}

class ImageRead {
  final File file;
  final String dataUrlContents;

  ImageRead(this.file, this.dataUrlContents);
}

@Component(
  selector: 'crop-image-component',
  templateUrl: 'crop_image_component.html',
  directives: [fnxUiDirectives,
  coreDirectives]
)
class CropImageComponent implements OnInit {

  @Input() ImageRead image;
  @Input() double ratio;

  final _cropped = new StreamController<Rectangle<double>>();
  @Output() Stream<Rectangle<double>> get cropped => _cropped.stream;

  Rectangle<double> crop = new Rectangle<double>(0.0, 0.0, 1.0, 1.0);


  @override
  ngOnInit() {
    if (ratio == null) throw "You must specify crop ratio";
  }

  void imageCropped(Rectangle<double> crop) {
    this.crop = crop;
  }
  
  void doFinish(Event event) {
    if (event != null) event.preventDefault();
    _cropped.add(crop);
  }

}
