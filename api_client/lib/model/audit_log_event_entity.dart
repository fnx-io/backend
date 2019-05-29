part of api_client.api;

class AuditLogEventEntity {
  int id = null;

  Key eventTarget = null;

  String message = null;

  DateTime occurredOn = null;

  Key changedBy = null;

  String changedByName = null;

  AuditLogEventEntity();

  @override
  String toString() {
    return 'AuditLogEventEntity[id=$id, eventTarget=$eventTarget, message=$message, occurredOn=$occurredOn, changedBy=$changedBy, changedByName=$changedByName, ]';
  }

  AuditLogEventEntity.fromJson(Map<String, dynamic> json) {
    if (json == null) return;
    id = json['id'];
    eventTarget = new Key.fromJson(json['eventTarget']);
    message = json['message'];
    occurredOn =
        json['occurredOn'] == null ? null : DateTime.parse(json['occurredOn']);
    changedBy = new Key.fromJson(json['changedBy']);
    changedByName = json['changedByName'];
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'eventTarget': eventTarget.toJson(),
      'message': message,
      'occurredOn':
          occurredOn == null ? '' : occurredOn.toUtc().toIso8601String(),
      'changedBy': changedBy.toJson(),
      'changedByName': changedByName
    };
  }

  static List<AuditLogEventEntity> listFromJson(List<dynamic> json) {
    return json == null
        ? new List<AuditLogEventEntity>()
        : json.map((value) => new AuditLogEventEntity.fromJson(value)).toList();
  }

  static Map<String, AuditLogEventEntity> mapFromJson(
      Map<String, Map<String, dynamic>> json) {
    var map = new Map<String, AuditLogEventEntity>();
    if (json != null && json.length > 0) {
      json.forEach((String key, Map<String, dynamic> value) =>
          map[key] = new AuditLogEventEntity.fromJson(value));
    }
    return map;
  }
}
