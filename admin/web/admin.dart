import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:admin/app.dart';
import 'package:admin/components.dart';
import 'package:admin/app_context.dart';

import 'package:angular2/angular2.dart';
import 'package:angular2/platform/browser.dart';
import 'package:angular2/platform/common.dart';
import 'package:angular2/router.dart';
import 'package:fnx_config/fnx_config_read.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/errors.dart';
import 'package:http/src/exception.dart';
import 'package:logging/logging.dart';

main() {
  // see https://pub.dartlang.org/packages/fnx_config
  String apiRoot = fnxConfig()["apiRoot"];

  // logging
  Logger.root.level = Level.ALL;
  Logger.root.onRecord.listen((LogRecord rec) {
    String message = '[${rec.level.name}]:'.padRight(8) + '${rec.loggerName}: ${rec.message}';
    if (rec.level >= Level.SEVERE) {
      window.console.error(message);
    } else {
      print(message);
      new Future.delayed(new Duration(seconds: 1), ()=>true);

    }
  });

  // Startup info
  Logger r = new Logger("admin.dart");
  r.info("================================================================");
  r.info(" Starting FNX admin:");
  r.info("   - apiRoot: ${apiRoot}");
  r.info("   - build: ${fnxConfigMeta()["timestamp"]}");
  r.info("================================================================");

  RestClient rest = new HttpRestClient.root(apiRoot);
  FnxExceptionHandler exceptionHandler = new FnxExceptionHandler();
  // ====== custom exceptions ===========================================
  exceptionHandler.registerErrorProcessor(Map, (exception, stacktrace) {
    Map e = exception as Map;

    String code = e['errorCode'];
    var body = e["errorMessage"];

    if (code == 'ValidationException') {
      List exceptions = JSON.decode(body);
      return new FnxError("Server refused to accept the data.",
          headline: "Invalid data",
          details: exceptions);
    } else {
      return new FnxError(e["errorMessage"], headline: "Error: ${e['errorCode']}");
    }
  });

  exceptionHandler.registerErrorProcessor(ClientException, (exception, stacktrace) {
    return new FnxError("Error while trying to connect to server.",
        headline: "Connection error"
    );
  });
  AppContext appCtx = new AppContext(apiRoot);

  // START!
  bootstrap(App, [
    ROUTER_PROVIDERS,
    CUSTOM_COMPONENTS,
    provide(LocationStrategy, useClass: HashLocationStrategy),
    provide(RestClient, useValue: rest),
    provide(ExceptionHandler, useValue: exceptionHandler),
    provide(FnxExceptionHandler, useValue: exceptionHandler),
    provide(AppContext, useValue: appCtx)
  ]);
}
