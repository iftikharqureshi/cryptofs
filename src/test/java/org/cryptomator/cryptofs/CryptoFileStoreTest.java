/*******************************************************************************
 * Copyright (c) 2016 Sebastian Stenzel and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE.txt.
 *
 * Contributors:
 *     Sebastian Stenzel - initial API and implementation
 *******************************************************************************/
package org.cryptomator.cryptofs;

import static org.cryptomator.cryptofs.common.UncheckedThrows.allowUncheckedThrowsOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

import org.cryptomator.cryptofs.attr.CryptoFileAttributeViewProvider;
import org.cryptomator.cryptofs.common.ReadonlyFlag;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CryptoFileStoreTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final Path path = mock(Path.class);
	private final CryptoFileAttributeViewProvider attributeViewProvider = mock(CryptoFileAttributeViewProvider.class);
	private final FileSystemProvider provider = mock(FileSystemProvider.class);
	private final FileSystem fileSystem = mock(FileSystem.class);
	private final FileStore delegate = mock(FileStore.class);
	private final ReadonlyFlag readonlyFlag = mock(ReadonlyFlag.class);

	@Before
	public void setUp() throws IOException {
		when(path.getFileSystem()).thenReturn(fileSystem);
		when(fileSystem.provider()).thenReturn(provider);
		when(provider.getFileStore(path)).thenReturn(delegate);
	}

	@Test
	public void testIsReadonlyReturnsTrueIfReadonlyFlagIsSet() throws IOException {
		when(readonlyFlag.isSet()).thenReturn(true);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		assertThat(inTest.isReadOnly(), is(true));
	}

	@Test
	public void testIsReadonlyReturnsFalseIfReadonlyFlagIsNotSet() throws IOException {
		when(readonlyFlag.isSet()).thenReturn(false);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		assertThat(inTest.isReadOnly(), is(false));
	}

	@Test
	public void testSupportedFileAttributeViewTypes() throws IOException {
		when(delegate.supportsFileAttributeView(PosixFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(FileOwnerAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(BasicFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(DosFileAttributeView.class)).thenReturn(false);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		Set<Class<? extends FileAttributeView>> result = inTest.supportedFileAttributeViewTypes();
		assertTrue(result.contains(PosixFileAttributeView.class));
		assertTrue(result.contains(FileOwnerAttributeView.class));
		assertTrue(result.contains(BasicFileAttributeView.class));
		assertFalse(result.contains(DosFileAttributeView.class));
	}

	@Test
	public void testSupportedFileAttributeViewNames() throws IOException {
		when(delegate.supportsFileAttributeView(PosixFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(FileOwnerAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(BasicFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(DosFileAttributeView.class)).thenReturn(false);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		Set<String> result = inTest.supportedFileAttributeViewNames();
		assertTrue(result.contains("posix"));
		assertTrue(result.contains("owner"));
		assertTrue(result.contains("basic"));
		assertFalse(result.contains("dos"));
	}

	@Test
	public void testSupportsFileAttributeViewByType() throws IOException {
		when(delegate.supportsFileAttributeView(PosixFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(FileOwnerAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(BasicFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(DosFileAttributeView.class)).thenReturn(false);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		assertTrue(inTest.supportsFileAttributeView(PosixFileAttributeView.class));
		assertTrue(inTest.supportsFileAttributeView(FileOwnerAttributeView.class));
		assertTrue(inTest.supportsFileAttributeView(BasicFileAttributeView.class));
		assertFalse(inTest.supportsFileAttributeView(DosFileAttributeView.class));
	}

	@Test
	public void testSupportsFileAttributeViewByName() throws IOException {
		when(delegate.supportsFileAttributeView(PosixFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(FileOwnerAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(BasicFileAttributeView.class)).thenReturn(true);
		when(delegate.supportsFileAttributeView(DosFileAttributeView.class)).thenReturn(false);

		CryptoFileStore inTest = allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));

		assertTrue(inTest.supportsFileAttributeView("posix"));
		assertTrue(inTest.supportsFileAttributeView("owner"));
		assertTrue(inTest.supportsFileAttributeView("basic"));
		assertFalse(inTest.supportsFileAttributeView("dos"));
	}

	@Test
	public void testRethrowsIOExceptionFromGetFileStoreUnchecked() throws IOException {
		IOException e = new IOException();
		when(provider.getFileStore(path)).thenThrow(e);

		thrown.expect(sameInstance(e));

		allowUncheckedThrowsOf(IOException.class).from(() -> new CryptoFileStore(path, readonlyFlag, attributeViewProvider));
	}

}
