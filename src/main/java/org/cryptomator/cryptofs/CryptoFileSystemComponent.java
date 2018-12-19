package org.cryptomator.cryptofs;

import dagger.Subcomponent;
import org.cryptomator.cryptofs.attr.CryptoFileAttributeViewComponent;
import org.cryptomator.cryptofs.file.OpenCryptoFileComponent;
import org.cryptomator.cryptofs.file.OpenCryptoFileModule;

@PerFileSystem
@Subcomponent(modules = {CryptoFileSystemModule.class})
public interface CryptoFileSystemComponent {

	CryptoFileSystemImpl cryptoFileSystem();

	OpenCryptoFileComponent newOpenCryptoFileComponent(OpenCryptoFileModule openCryptoFileModule);

	CryptoFileAttributeViewComponent.Builder newFileAttributeViewComponent();

}
