package org.cryptomator.cryptofs.file;

import static org.cryptomator.cryptofs.file.OpenCryptoFileModule.openCryptoFileModule;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.cryptomator.cryptofs.file.EffectiveOpenOptions;
import org.cryptomator.cryptofs.file.OpenCryptoFileModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class OpenCryptoFileModuleTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Path path = mock(Path.class);
	private EffectiveOpenOptions options = mock(EffectiveOpenOptions.class);

	@Test
	public void testBuilder() {
		OpenCryptoFileModule inTest = openCryptoFileModule() //
				.withOptions(options) //
				.withPath(path) //
				.build();

		assertThat(inTest.provideOptions(), is(options));
		assertThat(inTest.provideOriginalPath(), is(path));
		assertThat(inTest.provideCurrentPath().get(), is(path));
	}

	@Test
	public void testBuilderFailsIfPathIsMissing() {
		thrown.expect(IllegalStateException.class);

		openCryptoFileModule() //
				.withOptions(options) //
				.build();
	}

	@Test
	public void testBuilderFailsIfOptionsAreMissing() {
		thrown.expect(IllegalStateException.class);

		openCryptoFileModule() //
				.withPath(path) //
				.build();
	}

}
