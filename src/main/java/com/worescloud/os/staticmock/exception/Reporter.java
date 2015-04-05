package com.worescloud.os.staticmock.exception;

import org.mockito.exceptions.base.MockitoException;

import static org.mockito.internal.util.StringJoiner.join;

public class Reporter {

	public static void notAnEnhancedClass() {
		throw new MockitoException(join(
				"Argument passed to initMock() is not an enhanced class!",
				"Example of correct using:",
				"    StaticMockito.enhanceClass(\"package.NamedClass\");"
		));
	}

	public static void notAnInitializeEnhancedClass() {
		throw new MockitoException(join(
				"Argument passed to mocker() is an enhanced class, but not initialized!",
				"Example of correct using:",
				"    StaticMockito.initMock(NamedClass.class);"
		));
	}

}
