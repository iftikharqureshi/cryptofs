package org.cryptomator.cryptofs.fh;

import com.google.common.jimfs.Jimfs;
import org.cryptomator.cryptofs.EffectiveOpenOptions;
import org.cryptomator.cryptofs.ReadonlyFlag;
import org.cryptomator.cryptofs.ch.ChannelComponent;
import org.cryptomator.cryptofs.ch.CleartextFileChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OpenCryptoFileTest {

	private FileSystem fs;
	private AtomicReference<Path> currentFilePath;
	private ReadonlyFlag readonlyFlag = mock(ReadonlyFlag.class);
	private FileCloseListener closeListener = mock(FileCloseListener.class);
	private ChunkIO chunkIO = mock(ChunkIO.class);
	private AtomicLong fileSize = new AtomicLong();
	private AtomicReference<Instant> lastModified = new AtomicReference(Instant.ofEpochMilli(0));
	private OpenCryptoFileComponent openCryptoFileComponent = mock(OpenCryptoFileComponent.class);
	private ChannelComponent.Builder channelComponentBuilder = mock(ChannelComponent.Builder.class);
	private ChannelComponent channelComponent = mock(ChannelComponent.class);

	private OpenCryptoFile inTest;

	@BeforeEach
	public void setup() throws IOException {
		fs = Jimfs.newFileSystem("OpenCryptoFileTest");
		currentFilePath = new AtomicReference<>(fs.getPath("currentFile"));
		Mockito.when(openCryptoFileComponent.newChannelComponent()).thenReturn(channelComponentBuilder);
		Mockito.when(channelComponentBuilder.ciphertextChannel(Mockito.any())).thenReturn(channelComponentBuilder);
		Mockito.when(channelComponentBuilder.openOptions(Mockito.any())).thenReturn(channelComponentBuilder);
		Mockito.when(channelComponentBuilder.onClose(Mockito.any())).thenReturn(channelComponentBuilder);
		Mockito.when(channelComponentBuilder.build()).thenReturn(channelComponent);

		inTest = new OpenCryptoFile(closeListener, chunkIO, currentFilePath, fileSize, lastModified, openCryptoFileComponent);
	}

	@AfterEach
	public void tearDown() throws IOException {
		fs.close();
	}

	@Test
	public void testNewFileChannel() throws IOException {
		EffectiveOpenOptions options = EffectiveOpenOptions.from(EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), readonlyFlag);
		CleartextFileChannel cleartextFileChannel = mock(CleartextFileChannel.class);
		Mockito.when(channelComponent.channel()).thenReturn(cleartextFileChannel);

		FileChannel ch = inTest.newFileChannel(options);
		Assertions.assertEquals(cleartextFileChannel, ch);
	}

	@Test
	public void testCloseTriggersCloseListener() {
		inTest.close();
		verify(closeListener).close(currentFilePath.get(), inTest);
	}

}