/*******************************************************************************
 * Copyright (c) 2016, 2017 Sebastian Stenzel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE.txt.
 *
 * Contributors:
 *     Sebastian Stenzel - initial API and implementation
 *******************************************************************************/
package org.cryptomator.cryptofs.common;

public final class Constants {

	public static final int VAULT_VERSION = 6;
	public static final String MASTERKEY_BACKUP_SUFFIX = ".bkup";

	public static final String DATA_DIR_NAME = "d";
	public static final String METADATA_DIR_NAME = "m";
	public static final String DIR_PREFIX = "0";
	public static final int SHORT_NAMES_MAX_LENGTH = 129;
	public static final String ROOT_DIR_ID = "";

	public static final int MAX_SYMLINK_LENGTH = 32767; // max path length on NTFS and FAT32: 32k-1

	public static final String SEPARATOR = "/";

}
