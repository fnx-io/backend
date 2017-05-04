package io.fnx.backend.util;

import io.fnx.backend.util.conf.EnumItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Všechny nám známé číselníky.
 */
public class EnumerationRepository {

	private List<EnumItem> fileCategories;
	private List<EnumItem> roles;

	public List<EnumItem> getFileCategories() {
		return fileCategories;
	}

	public void setFileCategories(List<EnumItem> fileCategories) {
		this.fileCategories = fileCategories;
	}

	public List<EnumItem> getRoles() {
		return roles;
	}

	public void setRoles(List<EnumItem> roles) {
		this.roles = roles;
	}

	public static List<EnumItem> buildEnumerationFromEnum(Enum[] values, String prefix, MessageAccessor messageAccessor) {
		List<EnumItem> result = new ArrayList<>();
		for (Enum value : values) {
			result.add(new EnumItem(value.name(), messageAccessor.getMessage(prefix+"."+value.name())));
		}
		return result;
	}

}