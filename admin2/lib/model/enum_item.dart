class EnumItem {

  String value;
  String label;

  EnumItem(this.value, this.label);
  
  EnumItem.fromJson(Map json) : this(json['value'], json['label']);

  String toString() {
    return label;
  }

}