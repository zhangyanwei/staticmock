package com.worescloud.os.staticmock;

import java.util.Random;

public final class ClientUtility {

	public static String toClientId(String clientName) {
		return "clientName" + new Random().nextInt();
	}
}