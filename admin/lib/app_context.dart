import 'package:admin/messages/messages.i69n.dart';
import 'package:api_client/api.dart';

class AppContext {
  bool local;
  String authKey;

  // prihlaseny uzivatel
  Map loggedUser = null;

  Messages msg = null;

  final ClientConfiguration configData;

  AppContext(this.configData) {}

  bool get logged => loggedUser != null;

  Map<String, String> get serverMessages => configData.messages;

  EnumerationRepository get enumerations => configData.enumerations;

  /// remove logged user details
  void logout() {
    loggedUser = null;
  }
}
