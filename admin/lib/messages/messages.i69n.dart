// GENERATED FILE, do not edit!
import 'package:i69n/i69n.dart' as i69n;

String get _languageCode => 'en';
String get _localeName => 'en';

String _plural(int count,
        {String zero,
        String one,
        String two,
        String few,
        String many,
        String other}) =>
    i69n.plural(count, _languageCode,
        zero: zero, one: one, two: two, few: few, many: many, other: other);
String _ordinal(int count,
        {String zero,
        String one,
        String two,
        String few,
        String many,
        String other}) =>
    i69n.ordinal(count, _languageCode,
        zero: zero, one: one, two: two, few: few, many: many, other: other);
String _cardinal(int count,
        {String zero,
        String one,
        String two,
        String few,
        String many,
        String other}) =>
    i69n.cardinal(count, _languageCode,
        zero: zero, one: one, two: two, few: few, many: many, other: other);

class Messages implements i69n.I69nMessageBundle {
  const Messages();
  AppMessages get app => AppMessages(this);
  ProgramMessages get program => ProgramMessages(this);
  GenMessages get gen => GenMessages(this);
  Object operator [](String key) {
    int index = key.indexOf('.');
    if (index > 0) {
      return (this[key.substring(0, index)]
          as i69n.I69nMessageBundle)[key.substring(index + 1)];
    }
    switch (key) {
      case 'app':
        return app;
      case 'program':
        return program;
      case 'gen':
        return gen;
      default:
        throw new Exception("Message '$key' doesn't exist in $this");
    }
  }
}

class AppMessages implements i69n.I69nMessageBundle {
  final Messages _parent;
  const AppMessages(this._parent);
  String get name => "fnx.io";
  String get backend => "backend admin";
  Object operator [](String key) {
    int index = key.indexOf('.');
    if (index > 0) {
      return (this[key.substring(0, index)]
          as i69n.I69nMessageBundle)[key.substring(index + 1)];
    }
    switch (key) {
      case 'name':
        return name;
      case 'backend':
        return backend;
      default:
        throw new Exception("Message '$key' doesn't exist in $this");
    }
  }
}

class ProgramMessages implements i69n.I69nMessageBundle {
  final Messages _parent;
  const ProgramMessages(this._parent);
  String get mode => "TESTING";
  Object operator [](String key) {
    int index = key.indexOf('.');
    if (index > 0) {
      return (this[key.substring(0, index)]
          as i69n.I69nMessageBundle)[key.substring(index + 1)];
    }
    switch (key) {
      case 'mode':
        return mode;
      default:
        throw new Exception("Message '$key' doesn't exist in $this");
    }
  }
}

class GenMessages implements i69n.I69nMessageBundle {
  final Messages _parent;
  const GenMessages(this._parent);
  String get logout => "logout";
  String get test => "test";
  Object operator [](String key) {
    int index = key.indexOf('.');
    if (index > 0) {
      return (this[key.substring(0, index)]
          as i69n.I69nMessageBundle)[key.substring(index + 1)];
    }
    switch (key) {
      case 'logout':
        return logout;
      case 'test':
        return test;
      default:
        throw new Exception("Message '$key' doesn't exist in $this");
    }
  }
}
