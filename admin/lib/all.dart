import 'package:logging/logging.dart';

const allDirectives = [];

extension Logging on Object {
  Logger get log => Logger(this.runtimeType.toString());
}
