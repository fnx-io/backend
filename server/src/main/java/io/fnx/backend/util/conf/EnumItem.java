package io.fnx.backend.util.conf;

/**
 * Created by tomucha on 04.05.17.
 */
public class EnumItem {

	String value;
	String label;

	public EnumItem(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
