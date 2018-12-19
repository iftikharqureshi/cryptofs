package org.cryptomator.cryptofs.file;

import dagger.Subcomponent;

@Subcomponent(modules = {OpenCryptoFileModule.class})
@PerOpenFile
public interface OpenCryptoFileComponent {

	OpenCryptoFile openCryptoFile();

}
