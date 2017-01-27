import 'package:collection/collection.dart';


class UserRole {
  final String name;
  final String label;

  UserRole(this.name, this.label);

  static final ADMIN = new UserRole('ADMIN', 'Administrator');
  static final USER  = new UserRole('USER', 'Regular User');

  static final List<UserRole> ALL_ROLES = new UnmodifiableListView([ADMIN, USER]);

  bool operator == (other) => other != null && other.name == this.name;

  bool get admin => this == ADMIN;
}
