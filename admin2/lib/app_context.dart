import 'package:admin/model/enumeration_repository.dart';

class AppContext {

  bool local;
  String authKey;

  // prihlaseny uzivatel
  Map loggedUser = null;

  Map messages = null;
  Map<String,EnumerationRepository> enumerations;

  final Map<String, dynamic> configData;
  AppContext(this.configData) {
    messages = configData['messages'];
    enumerations = EnumerationRepository.buildFromAllEnumerations(configData['enumerations']);
  }


  bool get logged => loggedUser != null;

  /// remove logged user details
  void logout() {
    loggedUser = null;
  }
}
