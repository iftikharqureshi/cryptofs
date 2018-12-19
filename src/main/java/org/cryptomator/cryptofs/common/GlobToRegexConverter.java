package org.cryptomator.cryptofs.common;

import org.cryptomator.cryptofs.PerProvider;

import static org.cryptomator.cryptofs.common.Constants.SEPARATOR;

import javax.inject.Inject;

@PerProvider
public class GlobToRegexConverter {

	@Inject
	public GlobToRegexConverter() {
	}

	public String convert(String glob) {
		return GlobToRegex.toRegex(glob, SEPARATOR.charAt(0));
	}

}