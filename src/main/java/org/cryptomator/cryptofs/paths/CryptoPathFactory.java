package org.cryptomator.cryptofs.paths;

import java.text.Normalizer;
import java.util.Collections;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import org.cryptomator.cryptofs.CryptoFileSystemImpl;
import org.cryptomator.cryptofs.PerProvider;

import static java.util.Arrays.stream;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.cryptomator.cryptofs.common.Constants.SEPARATOR;

@PerProvider
public class CryptoPathFactory {

	@Inject
	public CryptoPathFactory() {
	}

	public CryptoPath getPath(CryptoFileSystemImpl fileSystem, String first, String... more) {
		boolean isAbsolute = first.startsWith(SEPARATOR);
		Stream<String> elements = Stream.concat(Stream.of(first), stream(more)).flatMap(this::splitPath).map(this::normalize);
		return new CryptoPath(fileSystem, elements.collect(toList()), isAbsolute);
	}

	public CryptoPath emptyFor(CryptoFileSystemImpl fileSystem) {
		return new CryptoPath(fileSystem, Collections.emptyList(), false);
	}

	public CryptoPath rootFor(CryptoFileSystemImpl fileSystem) {
		return new CryptoPath(fileSystem, Collections.emptyList(), true);
	}

	private Stream<String> splitPath(String path) {
		Iterable<String> tokens = Splitter.on(SEPARATOR).omitEmptyStrings().split(path);
		return stream(spliteratorUnknownSize(tokens.iterator(), ORDERED | IMMUTABLE | NONNULL), false);
	}

	private String normalize(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFC);
	}

}
