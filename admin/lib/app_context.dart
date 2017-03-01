
class AppContext {
  final String apiRoot;

  AppContext(this.apiRoot);

  // prihlaseny uzivatel
  Map loggedUser = null;
  Map config = null;

  bool get logged => loggedUser != null;

  /// remove logged user details
  void logout() {
    loggedUser = null;
  }
}
