import 'dart:html';

import 'package:admin/utils/file_tool.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/fnx_rest.dart';

mixin FileUploadMixin {
  RestClient get filesRest;

  RegExp specialChars = new RegExp(r"[^a-z0-9-_\.]+");

  String normalizeString(File source) {
    return source?.name?.toLowerCase()?.trim()?.replaceAll(specialChars, "_");
  }

  Future<FileEntity> upload(File file, {String set = "files", ProgressMonitor progressMonitor}) async {
    String normalizedFileName = normalizeString(file);
    String type = file.type;
    if (type == null || type.trim().isEmpty) type = "application/octet-stream";
    RestClient rc = filesRest.producesBinary(type);
    if (type.startsWith("image")) rc = rc.child("/image");
    RestResult rr = await rc
        .child("")
        .setParam("set", set)
        .streamedRequest("POST", file.size, FileTool.streamFile(file, progressMonitor: progressMonitor), headers: {'X-Filename': normalizedFileName, 'Content-type': type});
    return FileEntity.fromJson(rr.successData);
  }
}
