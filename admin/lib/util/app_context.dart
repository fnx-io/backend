import 'package:admin/model/enumeration_repository.dart';

class AppContext {

  final String apiRoot;

  // prihlaseny uzivatel
  Map loggedUser = null;

  Map messages = null;
  Map<String,EnumerationRepository> enumerations;

  AppContext(this.apiRoot);


  bool get logged => loggedUser != null;

  /// remove logged user details
  void logout() {
    loggedUser = null;
  }
}
