/*******************************************************************************
 * Copyright (c) 2016 Sebastian Stenzel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE.txt.
 *
 * Contributors:
 *     Sebastian Stenzel - initial API and implementation
 *******************************************************************************/
package org.cryptomator.cryptofs.attr;

import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.Optional;

import org.cryptomator.cryptofs.file.OpenCryptoFile;
import org.cryptomator.cryptolib.api.Cryptor;

class CryptoDosFileAttributes extends CryptoBasicFileAttributes implements DelegatingDosFileAttributes {

	private final DosFileAttributes delegate;

	public CryptoDosFileAttributes(DosFileAttributes delegate, Path ciphertextPath, Cryptor cryptor, Optional<OpenCryptoFile> openCryptoFile, boolean readonly) {
		super(delegate, ciphertextPath, cryptor, openCryptoFile, readonly);
		this.delegate = delegate;
	}

	@Override
	public DosFileAttributes getDelegate() {
		return delegate;
	}

	@Override
	public boolean isReadOnly() {
		if (readonly) {
			return true;
		} else {
			return delegate.isReadOnly();
		}
	}
}
