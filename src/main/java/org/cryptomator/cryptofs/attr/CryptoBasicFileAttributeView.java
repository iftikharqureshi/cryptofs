/*******************************************************************************
 * Copyright (c) 2016 Sebastian Stenzel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE.txt.
 *
 * Contributors:
 *     Sebastian Stenzel - initial API and implementation
 *******************************************************************************/
package org.cryptomator.cryptofs.attr;

import org.cryptomator.cryptofs.paths.CryptoPath;
import org.cryptomator.cryptofs.paths.CryptoPathMapper;
import org.cryptomator.cryptofs.file.OpenCryptoFiles;
import org.cryptomator.cryptofs.common.ReadonlyFlag;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

@PerAttributeView
class CryptoBasicFileAttributeView extends AbstractCryptoFileAttributeView implements BasicFileAttributeView {

	protected final CryptoFileAttributeProvider fileAttributeProvider;
	protected final ReadonlyFlag readonlyFlag;

	@Inject
	public CryptoBasicFileAttributeView(CryptoPath cleartextPath, CryptoPathMapper pathMapper, OpenCryptoFiles openCryptoFiles, CryptoFileAttributeProvider fileAttributeProvider, ReadonlyFlag readonlyFlag) {
		super(cleartextPath, pathMapper, openCryptoFiles);
		this.fileAttributeProvider = fileAttributeProvider;
		this.readonlyFlag = readonlyFlag;
	}

	@Override
	public String name() {
		return "basic";
	}

	@Override
	public BasicFileAttributes readAttributes() throws IOException {
		Path ciphertextPath = getCiphertextPath();
		return fileAttributeProvider.readAttributes(ciphertextPath, BasicFileAttributes.class);
	}

	@Override
	public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
		readonlyFlag.assertWritable();
		getCiphertextAttributeView(BasicFileAttributeView.class).setTimes(lastModifiedTime, lastAccessTime, createTime);
		if (lastModifiedTime != null) {
			getOpenCryptoFile().ifPresent(file -> file.setLastModifiedTime(lastModifiedTime));
		}
	}

}
