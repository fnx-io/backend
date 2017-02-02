import 'dart:async';
import 'dart:html';
import 'dart:typed_data';
import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'fnx-gallery-picker',
  templateUrl: 'fnx_gallery_picker.html',
  directives: const [ PickImageStageComponent,
                      CropImageComponent]
)
class FnxGalleryPicker {

  RestClient rest;
  RestListing listing;

  ImageUploadStage stage = ImageUploadStage.PICK;

  ImageRead imageRead;

  String errorMessage;

  FnxGalleryPicker(RestClient rest) {
    this.rest = rest.child("/v1/files");
    this.listing = RestListingFactory.withPaging(this.rest.child("?category=IMAGE"));
  }

  String thumbnailUrl(Map<String, dynamic> image) {
    if (image == null) return null;
    if (image['imageUrl'] != null) return "${image['imageUrl']}=s170";

    return image['bucketUrl'];
  }

  void imagePicked(ImageRead img) {
    errorMessage = null;
    imageRead = img;
    stage = ImageUploadStage.CROP;
  }

  void imageCropped(Rectangle<double> crop) {
    stage = ImageUploadStage.UPLOAD;
    final FileReader fr = new FileReader();
    fr.readAsArrayBuffer(imageRead.file);
    fr.onLoad.listen((_) => uploadImage(imageRead.file, fr.result));
    fr.onError.listen((_) => this.errorMessage = "File ${imageRead.file.name} could not be loaded.");
  }

  Future<bool> uploadImage(File file, Uint8List fileContents) async {
    RestClient rc = this.rest.child("/image").producesBinary(file.type);
    final RestResult rr = await rc.post(fileContents, headers: {'X-Filename': file.name, 'Content-Type': file.type});

    if (rr.success) {
      stage = ImageUploadStage.PICK;
      listing.refresh();
      return true;
    } else {
      return false;
    }
  }

  bool get showPickImageStage => stage == null || stage == ImageUploadStage.PICK;
  bool get showCropImageStage => stage == ImageUploadStage.CROP;
}

enum ImageUploadStage {
  PICK,
  CROP,
  UPLOAD
}

@Component(
  selector: 'pick-image-component',
  templateUrl: 'pick_image_component.html'
)
class PickImageStageComponent {

  String errorMessage;
  @Output() EventEmitter<ImageRead> image = new EventEmitter<ImageRead>();

  void filePicked(File file) {
    if (file == null) {
      errorMessage = 'Please pick an image to upload.';
      return;
    } else if (!validFile(file)) {
      errorMessage = "File ${file.name} is not supported image.";
      return;
    }
    final FileReader fr = new FileReader();
    fr.readAsDataUrl(file);
    fr.onLoad.listen((_) => this.image.emit(new ImageRead(file, fr.result)));
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
  templateUrl: 'crop_image_component.html'
)
class CropImageComponent {

  @Input() ImageRead image;

  @Output() EventEmitter<Rectangle<double>> cropped = new EventEmitter<Rectangle<double>>();
  @Output() EventEmitter<bool> cancel = new EventEmitter<bool>();

  Rectangle<double> crop = new Rectangle<double>(0.0, 0.0, 1.0, 1.0);

  void imageCropped(Rectangle<double> crop) {
    this.crop = crop;
  }

  void doCancel(Event event) {
    if (event != null) event.preventDefault();
    cancel.emit(true);
  }

  void doFinish(Event event) {
    if (event != null) event.preventDefault();
    cropped.emit(crop);
  }
}
