// GENERATED FILE (2019-05-12 14:28:47.626348), do not edit!
import 'package:i69n/i69n.dart' as i69n;

String get _languageCode => 'en';
String get _localeName => 'en';

String _plural(int count, {String zero, String one, String two, String few, String many, String other}) =>
    i69n.plural(count, _languageCode, zero:zero, one:one, two:two, few:few, many:many, other:other);
String _ordinal(int count, {String zero, String one, String two, String few, String many, String other}) =>
    i69n.ordinal(count, _languageCode, zero:zero, one:one, two:two, few:few, many:many, other:other);
String _cardinal(int count, {String zero, String one, String two, String few, String many, String other}) =>
    i69n.cardinal(count, _languageCode, zero:zero, one:one, two:two, few:few, many:many, other:other);

class Test {
  const Test();
  AppMessages get app => AppMessages(this);
  GenMessages get gen => GenMessages(this);
  ProgramMessages get program => ProgramMessages(this);
}

class AppMessages {
  final Test _parent;
  const AppMessages(this._parent);
  String get backend => "backend admin";
  String get name => "fnx.io";
}

class GenMessages {
  final Test _parent;
  const GenMessages(this._parent);
  String get logout => "logout";
}

class ProgramMessages {
  final Test _parent;
  const ProgramMessages(this._parent);
  String get mode => "TESTING";
}

