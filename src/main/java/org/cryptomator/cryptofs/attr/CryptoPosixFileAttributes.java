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
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import org.cryptomator.cryptofs.file.OpenCryptoFile;
import org.cryptomator.cryptolib.api.Cryptor;

import static java.nio.file.attribute.PosixFilePermission.GROUP_WRITE;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

class CryptoPosixFileAttributes extends CryptoBasicFileAttributes implements DelegatingPosixFileAttributes {

	private static final Set<PosixFilePermission> ALL_WRITE = EnumSet.of(OWNER_WRITE, GROUP_WRITE, OTHERS_WRITE);

	private final PosixFileAttributes delegate;

	public CryptoPosixFileAttributes(PosixFileAttributes delegate, Path ciphertextPath, Cryptor cryptor, Optional<OpenCryptoFile> openCryptoFile, boolean readonly) {
		super(delegate, ciphertextPath, cryptor, openCryptoFile, readonly);
		this.delegate = delegate;
	}

	@Override
	public PosixFileAttributes getDelegate() {
		return delegate;
	}

	@Override
	public Set<PosixFilePermission> permissions() {
		Set<PosixFilePermission> delegatePermissions = delegate.permissions();
		if (readonly) {
			return Sets.difference(delegatePermissions, ALL_WRITE);
		} else {
			return delegatePermissions;
		}
	}
}
