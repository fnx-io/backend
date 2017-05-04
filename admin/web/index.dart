import 'dart:async';
import 'dart:convert';
import 'dart:html';

import 'package:admin/app.dart';
import 'package:admin/components.dart';
import 'package:admin/model/enumeration_repository.dart';
import 'package:admin/util/app_context.dart';
import 'package:angular2/angular2.dart';
import 'package:angular2/platform/browser.dart';
import 'package:angular2/platform/common.dart';
import 'package:angular2/router.dart';
import 'package:fnx_config/fnx_config_read.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:fnx_ui/errors.dart';
import 'package:fnx_ui/i18n/fnx_messages_all.dart' as fnx_messages;
import 'package:http/src/exception.dart';
import 'package:intl/intl.dart';
import 'package:logging/logging.dart';

void main() {
  startApp().catchError((dynamic error) => window.alert("Unable to start application, please try again later: $error"));
}

Future<Null> startApp() async {

  // init translation of fnx_ui
  String locale = "en_US";
  Intl.defaultLocale = locale;
  fnx_messages.initializeMessages(locale);


  // Load compile time configuration (API endpoints and so)
  // see https://pub.dartlang.org/packages/fnx_config
  String apiRoot = fnxConfig()["apiRoot"];

  // configure logging
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
  r.info(" Starting fnx admin:");
  r.info("   - apiRoot: ${apiRoot}");
  r.info("   - build: ${fnxConfigMeta()["timestamp"]}");
  r.info("================================================================");

  // root rest client
  RestClient rest = new HttpRestClient.root(apiRoot);

  // exception handling
  FnxExceptionHandler exceptionHandler = new FnxExceptionHandler();
  registerCustomExceptionHandlers(exceptionHandler);

  // app context - save global state in here
  AppContext appCtx = new AppContext(apiRoot);

  // load remote configuration and enums
  RestResult rr = await rest.child("/v1/config").get();
  if (rr == null || rr.status != 200) {
    throw "Cannot load configuration from the server";
  }
  appCtx.messages = rr.data['messages'];
  appCtx.enumerations = EnumerationRepository.buildFromAllEnumerations(rr.data['enumerations']);
  r.info("Loaded configuration: ${appCtx.enumerations.keys}");

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

void registerCustomExceptionHandlers(FnxExceptionHandler exceptionHandler) {
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

  exceptionHandler.registerErrorProcessor(RestResult, (exception, stacktrace) {
    RestResult rr = exception as RestResult;
    if (rr.status == 403) return new FnxError("You don't have a permission to this operation.");
    if (rr.status == 404) return new FnxError("Requested record was not found, please refresh your browser.");
    if (rr.status >= 500) return new FnxError("Server error, please try again later");
    if (rr.data != null && rr.data['description'] != null) {
      return new FnxError(rr.data['description']);
    }
    return new FnxError("Unable to obtain data from server (${rr.status}: ${rr.data}).");
  });
}
