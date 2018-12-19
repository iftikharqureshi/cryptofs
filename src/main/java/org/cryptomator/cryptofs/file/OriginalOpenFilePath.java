package org.cryptomator.cryptofs.file;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The Path used to create an OpenCryptoFile
 * @see CurrentOpenFilePath
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@interface OriginalOpenFilePath {
}
