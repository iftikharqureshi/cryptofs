package org.cryptomator.cryptofs.attr;

import dagger.BindsInstance;
import dagger.Subcomponent;
import org.cryptomator.cryptofs.paths.CryptoPath;

import java.nio.file.attribute.FileAttributeView;
import java.util.Optional;

@PerAttributeView
@Subcomponent(modules = {CryptoFileAttributeViewModule.class})
public interface CryptoFileAttributeViewComponent {

	Optional<FileAttributeView> attributeView();

	@Subcomponent.Builder
	interface Builder {

		@BindsInstance
		Builder cleartextPath(CryptoPath cleartextPath);

		@BindsInstance
		Builder viewType(Class<? extends FileAttributeView> type);

		CryptoFileAttributeViewComponent build();
	}

}



