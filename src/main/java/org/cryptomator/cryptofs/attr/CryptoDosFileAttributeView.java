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
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;

@PerAttributeView
class CryptoDosFileAttributeView extends CryptoBasicFileAttributeView implements DosFileAttributeView {

	@Inject
	public CryptoDosFileAttributeView(CryptoPath cleartextPath, CryptoPathMapper pathMapper, OpenCryptoFiles openCryptoFiles, CryptoFileAttributeProvider fileAttributeProvider, ReadonlyFlag readonlyFlag) {
		super(cleartextPath, pathMapper, openCryptoFiles, fileAttributeProvider, readonlyFlag);
	}

	@Override
	public String name() {
		return "dos";
	}

	@Override
	public DosFileAttributes readAttributes() throws IOException {
		Path ciphertextPath = getCiphertextPath();
		return fileAttributeProvider.readAttributes(ciphertextPath, DosFileAttributes.class);
	}

	@Override
	public void setReadOnly(boolean value) throws IOException {
		readonlyFlag.assertWritable();
		getCiphertextAttributeView(DosFileAttributeView.class).setReadOnly(value);
	}

	@Override
	public void setHidden(boolean value) throws IOException {
		readonlyFlag.assertWritable();
		getCiphertextAttributeView(DosFileAttributeView.class).setHidden(value);
	}

	@Override
	public void setSystem(boolean value) throws IOException {
		readonlyFlag.assertWritable();
		getCiphertextAttributeView(DosFileAttributeView.class).setSystem(value);
	}

	@Override
	public void setArchive(boolean value) throws IOException {
		readonlyFlag.assertWritable();
		getCiphertextAttributeView(DosFileAttributeView.class).setArchive(value);
	}

}
