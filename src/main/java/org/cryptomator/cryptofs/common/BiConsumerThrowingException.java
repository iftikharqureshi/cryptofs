package org.cryptomator.cryptofs.common;

public interface BiConsumerThrowingException<A, B, E extends Exception> {

	void accept(A a, B b) throws E;

}
