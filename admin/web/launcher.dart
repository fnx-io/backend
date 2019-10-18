// Copyright (c) 2016, Tomucha. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:html' as html;

import 'package:admin/app_context.dart';
import 'package:admin/messages/messages.i69n.dart';
import 'package:admin/routing.dart';
import 'package:angular/angular.dart';
import 'package:angular_router/angular_router.dart';
import 'package:api_client/api.dart';
import 'package:fnx_rest/fnx_rest_browser.dart';
import 'package:fnx_ui/errors.dart';
import 'package:http/http.dart';
import 'package:logging/logging.dart';

import 'launcher.template.dart' as self;

// Local dev
@GenerateInjector([routerProvidersHash])
final InjectorFactory injectorWithHashStrategy = self.injectorWithHashStrategy$Injector;

// Production
@GenerateInjector([routerProviders])
final InjectorFactory injectorWithLocation = self.injectorWithLocation$Injector;


void launchApp<T>(ComponentFactory<T> componentFactory) async {
  Level currentLoggingLevel = Level.ALL;

  // nakonfigurujeme logování
  Logger.root.level = Level.ALL;
  Logger.root.onRecord.listen((LogRecord rec) {
    if (rec.level.value > currentLoggingLevel.value) {
      String msg =
          "${rec.loggerName}: ${rec.level.name}: ${rec.time} ${rec.message} ${rec.error ?? ''}";
      print(msg);
      if (rec.stackTrace != null) {
        print(rec.stackTrace);
      }
    }
  });

  Map<Object, Object> injections = {};
  injections[Routing] = new Routing();

  FnxExceptionHandler exceptionHandler = createExceptionHandler();
  injections[FnxExceptionHandler] = exceptionHandler;
  injections[ExceptionHandler] = exceptionHandler;

  //TODO: zjistovani jestli jsme na locale
  html.Location loc = html.window.location;
  bool isLocal = loc.href?.toLowerCase()?.contains('localhost') ?? false;

  RestClient restClient = new BrowserRestClient.root(
      isLocal ? "http://localhost:8085/api" : "/api");
  injections[RestClient] = restClient;

  AppContext appContext = await createAppContext(restClient);
  appContext.msg = new Messages();
  appContext.local = isLocal;

  injections[AppContext] = appContext;

  var injector = isLocal ? injectorWithHashStrategy : injectorWithLocation;

  runApp(componentFactory, createInjector: ([Injector parent]) => new Injector.map(injections, injector(parent)));
}

/*
Messagesabc getI69nMessages() {
  //here you can impements any logig for choosing message language
  //@see: https://pub.dartlang.org/packages/i69n
  return new Messagesabc();
}
*/

Future<AppContext> createAppContext(RestClient restClient) async {
  var configData = (await restClient.child("/v1/config").get()).successData;
  return new AppContext(ClientConfiguration.fromJson(configData));
}

FnxExceptionHandler createExceptionHandler() {
  var exceptionHandler = new FnxExceptionHandler();

  // ====== custom exceptions ===========================================
  exceptionHandler.registerErrorProcessor(Map, (exception, stacktrace) {
    Map e = exception as Map;
    return new FnxError(e["errorMessage"],
        headline: "Error: ${e['errorCode']}");
  });

  exceptionHandler.registerErrorProcessor(ClientException,
      (exception, stacktrace) {
    return new FnxError("Error while trying to connect to server.",
        headline: "Connection error");
  });

  exceptionHandler.registerErrorProcessor(HttpException,
      (exception, stacktrace) {
    HttpException e = exception as HttpException;
    return new FnxError('Error code: ${e.httpStatus}, message: "${e.data}"',
        headline: "Server error");
  });

  exceptionHandler.registerErrorProcessor(
      ExpressionChangedAfterItHasBeenCheckedException, (exception, stacktrace) {
    print(exception);
    print(stacktrace);
    return null;
  });

  return exceptionHandler;
}
