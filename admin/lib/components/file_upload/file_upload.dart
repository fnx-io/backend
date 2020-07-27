import 'dart:async';
import 'dart:html';

import 'package:admin/all.dart';
import 'package:admin/app_context.dart';
import 'package:angular/angular.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/src/rest_client.dart';
import 'package:fnx_ui/fnx_ui.dart';

import 'file_upload_mixin.dart';

///
/// Uploadne to sooubor na server, s krasnym progressbarem, a (file) emituje FileEntity,
/// se kterou si pak delej co chces. Nejspis ji pres EntityFileUpload mixin pripojis ke sve entite.
///
/// TODO: EntityFileUpload vyhodit a prilohy uplne zobecnit, aby se daly pripojit k cemukoliv
///
@Component(selector: 'file-upload', templateUrl: 'file_upload.html', directives: [
  coreDirectives,
  formDirectives,
  fnxUiAllDirectives,
  allDirectives,
], pipes: [])
class FileUpload with FileUploadMixin {
  final RestClient root;
  final AppContext ctx;

  FileUpload(this.root, this.ctx);

  StreamController<FileEntity> _fileController = StreamController<FileEntity>.broadcast();

  @Output()
  Stream<FileEntity> get file => _fileController.stream;

  double progress = null;

  Future<FileEntity> doUpload(File file) async {
    log.info("Uploading file");
    if (progress != null) return null; // ne, ted neco bezi, bez pryc
    progress = 0;
    FileEntity result = await upload(file, progressMonitor: (double p) => progress = p);
    _fileController.add(result);
    progress = null;
    log.info("... upload done.");
    return result;
  }

  @override
  // TODO: implement filesRest
  RestClient get filesRest => null;
}
