import 'dart:html';
import 'package:admin/app.template.dart' as ng;
import 'launcher.dart';

void main() async {
  try {
    await launchApp(ng.AppNgFactory);
  } catch (e, stacktrace) {
    print(e);
    print(stacktrace);
    window.alert("Nepodařilo se spustit aplikaci, aktualizujte okno: $e");
  }
}
