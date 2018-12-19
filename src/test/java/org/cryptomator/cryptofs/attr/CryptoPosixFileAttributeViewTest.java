package org.cryptomator.cryptofs.attr;

import static java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashSet;
import java.util.Set;

import org.cryptomator.cryptofs.paths.CryptoPath;
import org.cryptomator.cryptofs.paths.CryptoPathMapper;
import org.cryptomator.cryptofs.file.OpenCryptoFiles;
import org.cryptomator.cryptofs.common.ReadonlyFlag;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CryptoPosixFileAttributeViewTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	private Path ciphertextPath = mock(Path.class);
	private FileSystem fileSystem = mock(FileSystem.class);
	private FileSystemProvider fileSystemProvider = mock(FileSystemProvider.class);
	private PosixFileAttributeView delegate = mock(PosixFileAttributeView.class);

	private CryptoPath cleartextPath = mock(CryptoPath.class);
	private CryptoPathMapper pathMapper = mock(CryptoPathMapper.class);
	private OpenCryptoFiles openCryptoFiles = mock(OpenCryptoFiles.class);
	private CryptoFileAttributeProvider fileAttributeProvider = mock(CryptoFileAttributeProvider.class);
	private ReadonlyFlag readonlyFlag = mock(ReadonlyFlag.class);

	private CryptoPosixFileAttributeView inTest;

	@Before
	public void setUp() throws IOException {
		when(ciphertextPath.getFileSystem()).thenReturn(fileSystem);
		when(fileSystem.provider()).thenReturn(fileSystemProvider);
		when(fileSystemProvider.getFileAttributeView(ciphertextPath, PosixFileAttributeView.class)).thenReturn(delegate);
		when(fileSystemProvider.getFileAttributeView(ciphertextPath, BasicFileAttributeView.class)).thenReturn(delegate);

		when(pathMapper.getCiphertextFileType(cleartextPath)).thenReturn(CryptoPathMapper.CiphertextFileType.FILE);
		when(pathMapper.getCiphertextFilePath(cleartextPath, CryptoPathMapper.CiphertextFileType.FILE)).thenReturn(ciphertextPath);

		inTest = new CryptoPosixFileAttributeView(cleartextPath, pathMapper, openCryptoFiles, fileAttributeProvider, readonlyFlag);
	}

	@Test
	public void testNameReturnsPosix() {
		assertThat(inTest.name(), is("posix"));
	}

	@Test
	public void testGetOwnerDelegatesToDelegate() throws IOException {
		UserPrincipal expectedPrincipal = mock(UserPrincipal.class);
		when(delegate.getOwner()).thenReturn(expectedPrincipal);

		UserPrincipal result = inTest.getOwner();

		assertThat(result, is(expectedPrincipal));
	}

	@Test
	public void testSetOwnerDelegatesToDelegate() throws IOException {
		UserPrincipal expectedPrincipal = mock(UserPrincipal.class);

		inTest.setOwner(expectedPrincipal);

		verify(readonlyFlag).assertWritable();
		verify(delegate).setOwner(expectedPrincipal);
	}

	@Test
	public void testSetPermissionsDelegatesToDelegate() throws IOException {
		Set<PosixFilePermission> expectedPermissions = new HashSet<>(asList(OTHERS_WRITE));

		inTest.setPermissions(expectedPermissions);

		verify(readonlyFlag).assertWritable();
		verify(delegate).setPermissions(expectedPermissions);
	}

	@Test
	public void testSetGroupDelegatesToDelegate() throws IOException {
		GroupPrincipal expectedPricipal = mock(GroupPrincipal.class);

		inTest.setGroup(expectedPricipal);

		verify(readonlyFlag).assertWritable();
		verify(delegate).setGroup(expectedPricipal);
	}

	@Test
	public void testReadAttributesUsesProvider() throws IOException {
		PosixFileAttributes expectedAttributes = mock(PosixFileAttributes.class);
		when(fileAttributeProvider.readAttributes(ciphertextPath, PosixFileAttributes.class)).thenReturn(expectedAttributes);

		PosixFileAttributes result = inTest.readAttributes();

		assertThat(result, is(expectedAttributes));
	}

	@Test
	public void testSetTimesDelegatesToDelegate() throws IOException {
		FileTime lastModifiedTime = FileTime.fromMillis(39293923);
		FileTime lastAccessTime = FileTime.fromMillis(39293924);
		FileTime createTime = FileTime.fromMillis(39293925);

		inTest.setTimes(lastModifiedTime, lastAccessTime, createTime);

		verify(readonlyFlag).assertWritable();
		verify(delegate).setTimes(lastModifiedTime, lastAccessTime, createTime);
	}

}
