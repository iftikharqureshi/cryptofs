package org.cryptomator.cryptofs.common;

import java.nio.file.Path;

import org.cryptomator.cryptofs.CryptoFileSystemImpl;
import org.cryptomator.cryptofs.paths.CryptoPath;
import org.cryptomator.cryptofs.paths.CryptoPathFactory;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class TestHelper {

	public static void prepareMockForPathCreation(CryptoFileSystemImpl fileSystemMock, Path pathToVault) {
		CryptoPathFactory cryptoPathFactory = new CryptoPathFactory();
		Mockito.when(fileSystemMock.getPath(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenAnswer(invocation -> {
			String first = invocation.getArgument(0);
			if (invocation.getArguments().length == 1) {
				return cryptoPathFactory.getPath(fileSystemMock, first);
			} else {
				String[] more = invocation.getArgument(1);
				return cryptoPathFactory.getPath(fileSystemMock, first, more);
			}
		});
		Mockito.when(fileSystemMock.getPathToVault()).thenReturn(pathToVault);
		CryptoPath root = cryptoPathFactory.rootFor(fileSystemMock);
		CryptoPath empty = cryptoPathFactory.emptyFor(fileSystemMock);
		Mockito.when(fileSystemMock.getRootPath()).thenReturn(root);
		Mockito.when(fileSystemMock.getEmptyPath()).thenReturn(empty);
	}

}
