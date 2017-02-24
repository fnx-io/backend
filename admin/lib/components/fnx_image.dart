import 'dart:async';
import 'dart:html';
import 'dart:typed_data';
import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/fnx_ui.dart';

const CUSTOM_INPUT_IMAGE_VALUE_ACCESSOR = const Provider(NG_VALUE_ACCESSOR, useExisting: FnxImage, multi: true);

@Component(
  selector: 'fnx-image',
  templateUrl: 'fnx_image.html',
  providers: const [CUSTOM_INPUT_IMAGE_VALUE_ACCESSOR]
)
class FnxImage extends FnxInputComponent implements ControlValueAccessor, OnInit, OnDestroy {

  @Input()
  String title = "Choose or upload an image";

  @Input()
  bool required = false;

  bool openPicker = false;
  bool openDetail = false;

  FnxImage(FnxForm form, FnxInput wrapper) : super(form, wrapper);

  void openImagePicker(Event e) {
    e.stopPropagation();
    e.preventDefault();
    openPicker = true;
    markAsTouched();
  }

  void showImageDetail(Event e) {
    e.stopPropagation();
    e.preventDefault();
    openDetail = true;
  }

  void closeImagePicker() {
    openPicker = false;
    markAsTouched();
  }

  void pickedImage(String imgSrc) {
    value = imgSrc;
    closeImagePicker();
  }

  void removeImage() {
    value = null;
    markAsTouched();
  }

  @override
  bool hasValidValue() {
    if (!required) return true;
    return value != null;
  }
}
