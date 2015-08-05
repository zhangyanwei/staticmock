package com.worescloud.os.staticmock;

import java.util.Random;

import static java.lang.String.format;

public final class ClientUtility {

	public static String toClientId(String clientName) {
		return "clientName" + new Random().nextInt();
	}

	public static String toClientName(String clientId) {
		return format("clientName with ID (%s)", clientId);
	}
}