import 'dart:async';
import 'dart:html';
import 'dart:typed_data';

typedef void ProgressMonitor(double progress);

class FileTool {
  static Future<String> readFileDataUrl(File file) {
    final Completer<String> completer = new Completer();
    final FileReader reader = new FileReader();
    reader.onLoadEnd.listen((ProgressEvent event) {
      completer.complete(reader.result);
    });
    reader.readAsDataUrl(file);
    return completer.future;
  }

  static Future<Uint8List> readFileAsArrayBuffer(File file) {
    final Completer<Uint8List> completer = new Completer();
    final FileReader reader = new FileReader();
    reader.onLoadEnd.listen((ProgressEvent event) {
      completer.complete(reader.result);
    });
    reader.readAsArrayBuffer(file);
    return completer.future;
  }

  //nacte kus souboru vse ve future
  static Future<Uint8List> readSlicedFile(File file, int startingByte, int endingByte) {
    final Completer<Uint8List> completer = new Completer();
    final FileReader reader = new FileReader();
    reader.onLoad.listen((ProgressEvent event) {
      completer.complete(reader.result);
    });
    Blob blob = file.slice(startingByte, endingByte);
    reader.readAsArrayBuffer(blob);
    return completer.future;
  }

  //nakouskuje soubor a streje je po castech. Jedna se o asynchronni generator
  static Stream<Uint8List> streamFile<T>(File f, {ProgressMonitor progressMonitor}) async* {
    int step = 1024;
    int currentStep = 1;
    int total = f.size;
    int totalSteps = (total / step).ceil();

    while (currentStep <= totalSteps) {
      int offset = (currentStep - 1) * step;
      Uint8List ints = await readSlicedFile(f, offset, offset + step);

      currentStep++;
      if (progressMonitor != null) progressMonitor(currentStep / totalSteps);

      yield ints;
    }
    if (progressMonitor != null) progressMonitor(1.0);
  }
}
