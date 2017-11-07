import 'dart:html';

import 'package:admin/components/gallery_picker/fnx_gallery_picker.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/core.dart';
import 'package:fnx_ui/fnx_ui.dart';
import 'package:fnx_ui/src/validator.dart';

const CUSTOM_INPUT_GALLERY_VALUE_ACCESSOR = const Provider(NG_VALUE_ACCESSOR, useExisting: FnxGallery, multi: true);

@Component(
  selector: 'fnx-gallery',
  templateUrl: 'fnx_gallery.html',
  providers: const [CUSTOM_INPUT_GALLERY_VALUE_ACCESSOR],
  styles: const [
    ".delete-icon { opacity: 0 }",
    ".image:hover .delete-icon { opacity: 1 }"
  ]
)
class FnxGallery extends FnxInputComponent implements ControlValueAccessor, OnInit, OnDestroy {

  @Input()
  String title = "Choose or upload an image";

  @Input()
  bool required = false;

  @Input()
  bool disabled = false;

  @Input()
  bool readonly = false;

  @Input()
  FnxImageSet imageSet;

  bool openPicker = false;
  String openDetail = null;

  FnxGallery(FnxValidatorComponent parent) : super(parent);

  @override
  ngOnInit() {
    super.ngOnInit();
    if (imageSet == null) throw "You must specify 'set' attribute (instance of FnxImageSet)";
  }

  void openImagePicker(Event e) {
    e.stopPropagation();
    e.preventDefault();
    openPicker = true;
    markAsTouched();
  }

  void showImageDetail(Event e, String image) {
    e.stopPropagation();
    e.preventDefault();
    openDetail = image;
  }

  void closeImagePicker() {
    openPicker = false;
    markAsTouched();
  }

  void pickedImage(String imgSrc) {
    if (value == null || value is! List) {
      value = [];
    }
    value.add(imgSrc);
    closeImagePicker();
  }

  void removeImage(Event e, String imgSrc) {
    e.stopPropagation();
    e.preventDefault();
    if (value == null || value is! List) {
      value = [];
    }
    value.remove(imgSrc);
    markAsTouched();
  }

  @override
  bool hasValidValue() {
    if (!required) return true;
    if (value == null || value is! List) {
      value = [];
    }
    return value.isNotEmpty;
  }

  void onReorder(ReorderEvent e) {
    e.applyToList(value);
  }

}
