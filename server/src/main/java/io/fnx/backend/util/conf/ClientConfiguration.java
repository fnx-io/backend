package io.fnx.backend.util.conf;

import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.fnx.backend.domain.FileCategory;

import java.util.List;

import static java.lang.String.format;

/**
 *
 *  Use this class for sending configuration to Dart admin client. Enums, settings, flags, enabled modules etc.
 *  Anything you wish to manage from the server goes here.
 *
 */
public class ClientConfiguration {

	FileCategory[] fileCategories = FileCategory.values();

	public FileCategory[] getFileCategories() {
		return fileCategories;
	}

	public void setFileCategories(FileCategory[] fileCategories) {
		this.fileCategories = fileCategories;
	}
}
